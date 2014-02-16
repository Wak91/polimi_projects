/* 
 * File:   NRF24L01P.cpp
 * Author: degrigis
 * 
 * Created on January 9, 2014, 7:38 PM
 */

#include "NRF24L01P.h"
#include <miosix.h>
#include <miosix/kernel/scheduler/scheduler.h>
#include <stdio.h>
#include <stdlib.h>
#include "spi.h"
using namespace miosix;

/*
 * The following FIFOs are present in nRF24L01+:
 *   TX three level, 32 byte FIFO
 *   RX three level, 32 byte FIFO
 */
#define _NRF24L01P_TX_FIFO_COUNT   3
#define _NRF24L01P_RX_FIFO_COUNT   3
 
#define _NRF24L01P_TX_FIFO_SIZE   32
#define _NRF24L01P_RX_FIFO_SIZE   32
 
#define _NRF24L01P_SPI_MAX_DATA_RATE     10000000
 
//0xCommand from spi 
#define _NRF24L01P_SPI_R_REGISTER            0x00
#define _NRF24L01P_SPI_W_REGISTER            0x20
#define _NRF24L01P_SPI_CMD_RD_RX_PAYLOAD     0x61   
#define _NRF24L01P_SPI_CMD_WR_TX_PAYLOAD     0xa0
#define _NRF24L01P_SPI_CMD_FLUSH_TX          0xe1
#define _NRF24L01P_SPI_CMD_FLUSH_RX          0xe2
#define _NRF24L01P_SPI_CMD_REUSE_TX_PL       0xe3
#define _NRF24L01P_SPI_CMD_R_RX_PL_WID       0x60
#define _NRF24L01P_SPI_CMD_W_ACK_PAYLOAD     0xa8
#define _NRF24L01P_SPI_CMD_W_TX_PYLD_NO_ACK  0xb0
#define _NRF24L01P_SPI_CMD_NOP               0xff
 
//Registers define address
#define _NRF24L01P_REG_CONFIG                0x00
#define _NRF24L01P_REG_EN_AA                 0x01
#define _NRF24L01P_REG_EN_RXADDR             0x02
#define _NRF24L01P_REG_SETUP_AW              0x03
#define _NRF24L01P_REG_SETUP_RETR            0x04
#define _NRF24L01P_REG_RF_CH                 0x05
#define _NRF24L01P_REG_RF_SETUP              0x06
#define _NRF24L01P_REG_STATUS                0x07
#define _NRF24L01P_REG_OBSERVE_TX            0x08
#define _NRF24L01P_REG_RPD                   0x09
#define _NRF24L01P_REG_RX_ADDR_P0            0x0a
#define _NRF24L01P_REG_RX_ADDR_P1            0x0b
#define _NRF24L01P_REG_RX_ADDR_P2            0x0c
#define _NRF24L01P_REG_RX_ADDR_P3            0x0d
#define _NRF24L01P_REG_RX_ADDR_P4            0x0e
#define _NRF24L01P_REG_RX_ADDR_P5            0x0f
#define _NRF24L01P_REG_TX_ADDR               0x10
#define _NRF24L01P_REG_RX_PW_P0              0x11
#define _NRF24L01P_REG_RX_PW_P1              0x12
#define _NRF24L01P_REG_RX_PW_P2              0x13
#define _NRF24L01P_REG_RX_PW_P3              0x14
#define _NRF24L01P_REG_RX_PW_P4              0x15
#define _NRF24L01P_REG_RX_PW_P5              0x16
#define _NRF24L01P_REG_FIFO_STATUS           0x17
#define _NRF24L01P_REG_DYNPD                 0x1c
#define _NRF24L01P_REG_FEATURE               0x1d
 
#define _NRF24L01P_REG_ADDRESS_MASK          0x1f
 
// CONFIG register:
#define _NRF24L01P_CONFIG_PRIM_RX        (1<<0) // 0000 0001 = 0x01
#define _NRF24L01P_CONFIG_PWR_UP         (1<<1) // 0000 0010 = 0x02
#define _NRF24L01P_CONFIG_CRC0           (1<<2) // 0000 0100 = 0x04
#define _NRF24L01P_CONFIG_EN_CRC         (1<<3) // 0000 1000 = 0x08
#define _NRF24L01P_CONFIG_MASK_MAX_RT    (1<<4) // 0001 0000 = 0x10
#define _NRF24L01P_CONFIG_MASK_TX_DS     (1<<5) // 0010 0000 = 0x20
#define _NRF24L01P_CONFIG_MASK_RX_DR     (1<<6) // 0100 0000 = 0x40

//Mask to delete single bits in CONFIG register 
#define _NRF24L01P_CONFIG_NO_PRIM_RX     0xfe   // 1111 1110
#define _NRF24L01P_CONFIG_NO_PWR_UP      0xfd   // 1111 1101
#define _NRF24L01P_CONFIG_NO_CRC0        0xfb   // ... 
#define _NRF24L01P_CONFIG_NO_EN_CRC      0xf7
#define _NRF24L01P_CONFIG_NO_MASK_MAX_RT 0xef
#define _NRF24L01P_CONFIG_NO_MASK_TX_DS  0xdf
#define _NRF24L01P_CONFIG_NO_MASK_RX_DR  0xbf
 

 
// EN_AA register:
#define _NRF24L01P_EN_AA_NONE            0
 
// EN_RXADDR register:
#define _NRF24L01P_EN_RXADDR_NONE        0
 
// SETUP_AW register:
#define _NRF24L01P_SETUP_AW_AW_MASK      (0x3<<0)
#define _NRF24L01P_SETUP_AW_AW_3BYTE     (0x1<<0)
#define _NRF24L01P_SETUP_AW_AW_4BYTE     (0x2<<0)
#define _NRF24L01P_SETUP_AW_AW_5BYTE     (0x3<<0)
 
 
// STATUS register:
#define _NRF24L01P_STATUS_TX_FULL        (1<<0)
#define _NRF24L01P_STATUS_RX_P_NO        (0x7<<1)
#define _NRF24L01P_STATUS_MAX_RT         (1<<4)
#define _NRF24L01P_STATUS_TX_DS          (1<<5)
#define _NRF24L01P_STATUS_RX_DR          (1<<6)
 
// RX_PW_P0..RX_PW_P5 registers:
#define _NRF24L01P_RX_PW_Px_MASK         0x3F
 
#define _NRF24L01P_TIMING_Tundef2pd_us     100000   // 100mS
#define _NRF24L01P_TIMING_Tstby2a_us          130   // 130uS
#define _NRF24L01P_TIMING_Thce_us              10   //  10uS
#define _NRF24L01P_TIMING_Tpd2stby_us        4500   // 4.5mS worst case
#define _NRF24L01P_TIMING_Tpece2csn_us          4   //   4uS

typedef Gpio <GPIOB_BASE,15> mosi;
typedef Gpio <GPIOB_BASE,14> miso;
typedef Gpio <GPIOB_BASE,13> sck;
typedef Gpio <GPIOB_BASE,12> cs;
typedef Gpio <GPIOB_BASE,11> ce;
typedef Gpio <GPIOA_BASE,1> irq;

//DEBUG
typedef Gpio <GPIOD_BASE, 15> bluLed;

typedef Gpio <GPIOD_BASE, 14> redLed;

typedef Gpio <GPIOD_BASE, 13> orangeLed;



// thanks to this enum I can check the status of transceiver via software
// unless read the register.
typedef enum {
_NRF24L01P_MODE_UNDEFINED,
_NRF24L01P_MODE_POWER_DOWN,
_NRF24L01P_MODE_STANDBY,
_NRF24L01P_MODE_RX_MODE,
_NRF24L01P_MODE_TX_MODE,
} NRF24L01P_MODE;


typedef enum {
    _TX_FIFO_EMPTY,
    _TX_FIFO_NOT_EMPTY,
    _TX_FIFO_FULL // only when all slots are busy
} TX_FIFO_STATE;


int nrf24l01p_mode;
int tx_fifo_status;
char data_rx;

NRF24L01P::NRF24L01P() {
   bluLed::mode(Mode::OUTPUT);
   redLed::mode(Mode::OUTPUT);
   orangeLed::mode(Mode::OUTPUT);
   spiDriver = new spi();
   mosi::mode(Mode::ALTERNATE);
   mosi::alternateFunction(5); 
   miso::mode(Mode::ALTERNATE);
   miso::alternateFunction(5);
   sck::mode(Mode::ALTERNATE);
   sck::alternateFunction(5);
   cs::mode(Mode::OUTPUT);
   ce::mode(Mode::OUTPUT);
   cs::high();
   ce::low();
   
   nrf24l01p_mode = _NRF24L01P_MODE_UNDEFINED; // starts with an undefined state
   tx_fifo_status = _TX_FIFO_EMPTY; // the tx_fifo starts empty obviously
}

NRF24L01P::NRF24L01P(const NRF24L01P& orig) {   
}

NRF24L01P::~NRF24L01P() {
}

/**
 * This function put the transceiver in stand by I mode ( see charts ) 
 * Have to improve this function with a customizable power_up settings 
 */
void NRF24L01P::powerUp()
{
   int current_config , new_config;
   current_config = readRegister(_NRF24L01P_REG_CONFIG); // retreive the current status of CONFIG register
   new_config = current_config | _NRF24L01P_CONFIG_PWR_UP; // put to 1 the PWR_UP bit in CONFIG register
   
   writeRegister(_NRF24L01P_REG_CONFIG, new_config );
   
   //now have to wait to worst time from power_down to standby ( charts for info )
   
   usleep(_NRF24L01P_TIMING_Tpd2stby_us);
   
   //writeRegister(_NRF24L01P_REG_TX_ADDR , 10); //customize the two register with same data
   //writeRegister(_NRF24L01P_REG_RX_ADDR_P0 ,20);
   
   
   nrf24l01p_mode = _NRF24L01P_MODE_STANDBY; // now we are in standby mode 
   
   
   //DEBUG TRANSMISSION
   
   //this->writeRegister(_NRF24L01P_REG_RX_PW_P0, 1);
   //this->writeRegister(_NRF24L01P_REG_RX_PW_P1, 1);
   //this->writeRegister( _NRF24L01P_REG_SETUP_AW,1);
   
   //DEBUG TRANSMISSION
   bluLed::high();
}

void NRF24L01P::configureTxAddress(int number)
{
    int n_bit = number-2; // see register means
    
    writeRegister(_NRF24L01P_REG_SETUP_AW, n_bit );
    
}

void NRF24L01P::powerDown()
{
 
int current_config , new_config;
current_config = readRegister(_NRF24L01P_REG_CONFIG); // retreive the current status of CONFIG register
new_config = current_config & _NRF24L01P_CONFIG_NO_PWR_UP; // put to 0 the PWR_UP bit in CONFIG register

writeRegister(_NRF24L01P_REG_CONFIG, new_config );
nrf24l01p_mode = _NRF24L01P_MODE_POWER_DOWN; 

}

void NRF24L01P::resetModule()
{
    redLed::high();
    ce::low();
            
    this->flushRx();
    this->flushTx();
    
    writeRegister(_NRF24L01P_REG_CONFIG, 8); //reset config_register to 00001000
    writeRegister(_NRF24L01P_REG_EN_AA, 63);
    writeRegister(_NRF24L01P_REG_EN_RXADDR, 3);
    writeRegister(_NRF24L01P_REG_SETUP_AW,3);
    writeRegister(_NRF24L01P_REG_SETUP_RETR,3);
    writeRegister(_NRF24L01P_REG_RF_CH, 2);
    writeRegister(_NRF24L01P_REG_RF_SETUP,14);
    writeRegister(_NRF24L01P_REG_STATUS,14);
    writeRegister(_NRF24L01P_REG_OBSERVE_TX, 0);
    writeRegister(_NRF24L01P_REG_RPD, 0);
    writeRegister(_NRF24L01P_REG_RX_ADDR_P0, 996028180455);
    writeRegister(_NRF24L01P_REG_RX_ADDR_P1, 836491199170);
    writeRegister(_NRF24L01P_REG_RX_ADDR_P2, 195);
    writeRegister(_NRF24L01P_REG_RX_ADDR_P3, 196);
    writeRegister(_NRF24L01P_REG_RX_ADDR_P4, 197);
    writeRegister(_NRF24L01P_REG_RX_ADDR_P5, 198);
    writeRegister(_NRF24L01P_REG_TX_ADDR, 996028180455 );
    writeRegister(_NRF24L01P_REG_RX_PW_P0,0);
    writeRegister(_NRF24L01P_REG_RX_PW_P1,0);
    writeRegister(_NRF24L01P_REG_RX_PW_P2,0);
    writeRegister(_NRF24L01P_REG_RX_PW_P3,0);
    writeRegister(_NRF24L01P_REG_RX_PW_P4,0);
    writeRegister(_NRF24L01P_REG_RX_PW_P5,0);
    writeRegister( _NRF24L01P_REG_FIFO_STATUS,17);
    writeRegister(_NRF24L01P_REG_DYNPD, 0);
    writeRegister(_NRF24L01P_REG_FEATURE,0);
 
    redLed::low();
    bluLed::high();

}

/**
 * Used to return in standby-i mode from the ReceiveMode
 * The return from TransmitMode to sandby-i mode is automatically performed by Enhanced ShockBurst 
 */
void NRF24L01P::returnStandByI()
{
    ce::low();
    nrf24l01p_mode = _NRF24L01P_MODE_STANDBY;
}

/**
 * Remember that the RF channel is set according to formula: F0 = 2400 + RF_CH [ MHZ ]
 * the frequency are from 2,4 GHZ to 2,525GHZ, so the offset is between 0 and 125
 */
void NRF24L01P::setRfChannel(int offset)
{
    if(offset < 0 || offset >125)
        printf("Wrong offset\n");
    writeRegister(_NRF24L01P_REG_RF_CH,offset );

}

/**
 * AirDataRate    air_rate
 *   250kbps         38      better receiver sensitivity 
 *    1Mbps           6
 *    2Mbps          14      lower power consumption and low collision
 *   
 */
void NRF24L01P::setAirDataRate(int air_rate)
{
    if(air_rate == 38 || air_rate == 6 || air_rate == 14) // must be a valid air data rate 
        writeRegister(_NRF24L01P_REG_RF_SETUP , air_rate);
    else
        printf("Wrong air_rate value\n");
}



/**
 In order to call this function you have to call power_up, PWR_UP MUST be 1! 
 */
void NRF24L01P::setReceiveMode()
{
   int current_config , new_config;

   current_config = readRegister(_NRF24L01P_REG_CONFIG); // retreive the current status of CONFIG register
   
   if( nrf24l01p_mode != _NRF24L01P_MODE_STANDBY ) // security control, if somebody call this function before settings transceiver
     {
       fprintf(stderr, "You have to call powerUp first\n");
       return;
     }
   
   new_config = current_config | _NRF24L01P_CONFIG_PRIM_RX ; // put to 1 the PRIM_RX bit in CONFIG register 
   
   writeRegister(_NRF24L01P_REG_CONFIG, new_config);
   ce::high();
   
   usleep(_NRF24L01P_TIMING_Tstby2a_us);
   
   usleep(40); // sleeping to activate RPD
   
   
   nrf24l01p_mode = _NRF24L01P_MODE_RX_MODE;
   
}

/**
 * used to send data to TX_FIFO_BUFFER through the SPI
 * @param data: data is a char pointer to the data to send, if it is a integer it scan byte per byte the integer
 */
void NRF24L01P::TrasmitData(char *data , int dim)
{
    int i;
    int original_ce = ce::value();
    ce::low();
    
    if(dim > _NRF24L01P_TX_FIFO_SIZE  ) dim = _NRF24L01P_TX_FIFO_SIZE; //check the size of data
   
    printf("Have to transmit %c\n", *data);
    
    int current_config = this->readStatusRegister();
    int new_config = current_config | _NRF24L01P_STATUS_TX_DS; // reset TX_DS
    writeRegister(_NRF24L01P_REG_STATUS,  new_config );
    
    cs::low(); //---SPI START! 
    
    spiDriver->send(_NRF24L01P_SPI_CMD_WR_TX_PAYLOAD ); // request to write on the TX_BUFFER a payload
    
    for(i=0;i<dim;i++) 
        {
         spiDriver->send(*data++);
        }
    
    cs::high(); //---SPI END!
    
    int OriginalMode = nrf24l01p_mode; // save the current mode of nrf
    
    int result =  this->readStatusRegister();
    printf("il registro status prima di aver trasmesso è: %d\n" , result);
    
    this->setTransmitMode(); // and switch to transmission mode
    ce::high();
    usleep(_NRF24L01P_TIMING_Thce_us);
    ce::low(); 
    
    result = this->readRegister(23);
    printf("il registro FIFO_STATUS_TX prima di aver trasmesso è: %d\n" , result);
    
    while(!(this->readStatusRegister() & _NRF24L01P_STATUS_TX_DS ))
         {
          printf("waiting tx_ds\n");// Wait untill IRQ set the TX_DS to 1.
         }
    
    result =  this->readStatusRegister();
    //printf("**************TRANSMISSION COMPLETE******************");
    //printf("il registro status dopo aver trasmesso è: %d\n" , result);
    
    //result = this->readRegister(23);
    //printf("il registro FIFO_STATUS_TX dopo aver trasmesso è: %d\n" , result);
    
    current_config = this->readStatusRegister();
    new_config = current_config | _NRF24L01P_STATUS_TX_DS; // reset TX_DS for next IRQ
    writeRegister(_NRF24L01P_REG_STATUS,  new_config );
    
    //if(OriginalMode == _NRF24L01P_MODE_RX_MODE )
      //{ now comment, it will be useful later! 
        this->setReceiveMode();
     // }
    
    //if(original_ce == 1 ) 
     //ce::high();
    //else 
     ce::low();
    
    usleep(_NRF24L01P_TIMING_Tpece2csn_us);
    
}

void NRF24L01P::flushTx()
{
  printf("--->FLUSH TX-BUFFER\n");
  cs::low();
  spiDriver->send( _NRF24L01P_SPI_CMD_FLUSH_TX  );  //svuoto coda TX
  cs::high();
  tx_fifo_status = _TX_FIFO_EMPTY;
}

void NRF24L01P::flushRx()
{
  printf("--->FLUSH RX-BUFFER\n");
  cs::low();
  spiDriver->send( _NRF24L01P_SPI_CMD_FLUSH_RX  );  //svuoto coda TX
  cs::high();
  tx_fifo_status = _TX_FIFO_EMPTY;


}

void NRF24L01P::NoAckOnThisPack()
{
  printf("Removing ack on this packet \n");
  cs::low();
  spiDriver->send( _NRF24L01P_SPI_CMD_W_TX_PYLD_NO_ACK  );  // Don't want ack on this packet 
  cs::high();

}
/**
 * used to take data from RX_FIFO_BUFFER through SPI 
 * @return 
 */
int NRF24L01P::receiveDataFromRx()
{   
    int content;
    cs::low();
    spiDriver->send(_NRF24L01P_SPI_CMD_RD_RX_PAYLOAD);
    content = spiDriver->receive();
    cs::high();
    
    return content;
}

/**
 * This function is used to read the RPD bit that 
 * tells if a valid RF signal >-64dbm is detected. 
 * This means that a NRF24L01p is near us, so we can send our payload.
 * @return 
 */
int NRF24L01P::readRPD()
{
   if( nrf24l01p_mode != _NRF24L01P_MODE_RX_MODE ) // security check
     {
       fprintf(stderr, "Failed to read RPD, must be in RX_MODE\n");
       return 0;
     }
   
   return readRegister(_NRF24L01P_REG_RPD);
    
}



/**
 * Remember to call this function after call to returnStandBy-I
 * Before call this function a packet in TX must be present, so I check if TX is empty or not 
 */
void NRF24L01P::setTransmitMode()
{
  this->returnStandByI();
  int current_config , new_config;
  current_config = readRegister(_NRF24L01P_REG_CONFIG);
  new_config = current_config & _NRF24L01P_CONFIG_NO_PRIM_RX; // actually, this is 1111 1110 and preserve previous config, deleting the PRIM_RX
  
  writeRegister(_NRF24L01P_REG_CONFIG , new_config );
  
  nrf24l01p_mode = _NRF24L01P_MODE_TX_MODE;
  orangeLed::high();
  
  //here now starts the transmission packet flow charts, remember to change mode in the function
  //that handles the interrupt and to set the TX_FIFO to empty ( if empty naturally )
}

void NRF24L01P::writeRegister(int regAddress, int regData)
{
    this->returnStandByI();
    // aggiungere controllo ce e ripristinarlo alla fine di questa funzione 
    int request;
    request = regAddress | _NRF24L01P_SPI_W_REGISTER;
   
    cs::low();
    
    spiDriver->send(request);
    spiDriver->send(regData); 
    
    cs::high();
    
    
}

/**
 * This function read a general register on the NRF24L01P
 * 
 * @param regAddress: the address of the register, see above for the define
 * @return : the value in hex of the register
 * 
 */
int NRF24L01P::readRegister(int regAddress) //arriva su 1 byte
{
    // request = regAddress; tanto questo è in | bit a bit con 0x00 ( READ ) e in & bit a bit con una MASK 0x1f
    int value;
    
    //every spi commands must starts with a high to low cs signal
    cs::low();
    
    spiDriver->send(regAddress);
    value = spiDriver->receive();
    
    cs::high();
    
    return value;
}

void NRF24L01P::showInternal()
{   
    int result;
    
    result = this->readRegister(0);
    printf("<<CONFIG REGISTER>> è: %d\n" , result);
    
    result = this->readRegister(1);
    printf("EN_AA REGISTER è: %d\n" , result);
    
    result = this->readRegister(2);
    printf("EN_RXADDR REGISTER è: %d\n" , result);
    
    result = this->readRegister(3);
    printf("SETUP_AW REGISTER è: %d\n" , result);
    
    result = this->readRegister(4);
    printf("SETUP_RETR REGISTER è: %d\n" , result);
    
    result = this->readRegister(5);
    printf("RF_CH REGISTER è: %d\n" , result);
    
    result = this->readRegister(6);
    printf("RF_SETUP REGISTER è: %d\n" , result);
    
    result = this->readRegister(7);
    printf("<<STATUS REGISTER>> è: %d\n" , result);
    
    result = this->readRegister(8);
    printf("OBSERVE_TX REGISTER è: %d\n" , result);
    
    result = this->readRegister(9);
    printf("<<RPD REGISTER>> è: %d\n" , result);
    
    result = this->readRegister(10);
    printf("RX_ADDR_P0 REGISTER è: %d\n" , result);
    
     result = this->readRegister(11);
    printf("RX_ADDR_P1 REGISTER è: %d\n" , result);
    
     result = this->readRegister(12);
    printf("RX_ADDR_P2 REGISTER è: %d\n" , result);
    
     result = this->readRegister(13);
    printf("RX_ADDR_P3 REGISTER è: %d\n" , result);
    
     result = this->readRegister(14);
    printf("RX_ADDR_P4 REGISTER è: %d\n" , result);
    
     result = this->readRegister(15);
    printf("RX_ADDR_P5 REGISTER è: %d\n" , result);
            
     result = this->readRegister(16);
    printf("TX_ADDR REGISTER è: %d\n" , result);
    
    result = this->readRegister(17);
    printf("RX_PW_P0 REGISTER è: %d\n" , result);
    
    result = this->readRegister(18);
    printf("RX_PW_P1 REGISTER è: %d\n" , result);
    
    result = this->readRegister(19);
    printf("RX_PW_P2 REGISTER è: %d\n" , result);
    
    result = this->readRegister(20);
    printf("RX_PW_P3 REGISTER è: %d\n" , result);
   
    result = this->readRegister(21);
    printf("RX_PW_P4 REGISTER è: %d\n" , result);
    
    result = this->readRegister(22);
    printf("RX_PW_P5 REGISTER è: %d\n" , result);
    
    result = this->readRegister(23);
    printf("<<FIFO_STATUS>> REGISTER è: %d\n" , result);
    
    result = this->readRegister(29);
    printf("FEATURE REGISTER è: %d\n" , result);

}
/**
 * 
 * the status register is passe via miso after a dummy write on mosi
 * @return : the value in hex of status register of NRF24L01P
 */
int NRF24L01P::readStatusRegister()
{
    int value;
    cs::low();
    value=spiDriver->receive(); // in this there is a dummy write of 0 on MOSI
    cs::high();
    return value; 

}

char NRF24L01P::readData()
{
    return data_rx;
}

void __attribute__((naked)) EXTI1_IRQHandler()
{
    saveContext();
    asm volatile("bl _Z16EXTI1HandlerImplv");
    restoreContext();
}

void __attribute__((used)) EXTI1HandlerImpl()
{
    EXTI->PR=EXTI_PR_PR1; //viene resettato il registro che permette di uscire dalla chiamata a interrupt 
    if(NRF24L01P.readStatusRegister() & 0x40 == 0)
       {
        char data = (char) NRF24L01P.receiveDataFromRx(); // put the data in a local var
        data_rx = data; // then store local var in the global var 
       }
    else
        if(NRF24L01P.readStatusRegister() & _NRF24L01P_STATUS_TX_DS)
           {
            bluLed::high();
            sleep(20);
            bluLed::low();
            redLed::high();
            sleep(20);
            redLed::low();
            orangeLed::high();
            sleep(20);
            orangeLed::low();
           }
    
}


void NRF24L01P::configureInterrupt()
{
    irq::mode(Mode::INPUT);
    EXTI->IMR |= EXTI_IMR_MR1;
    EXTI->RTSR |= EXTI_RTSR_TR1;
    NVIC_EnableIRQ(EXTI1_IRQn);
    NVIC_SetPriority(EXTI1_IRQn,15); //Low priority
    
}






