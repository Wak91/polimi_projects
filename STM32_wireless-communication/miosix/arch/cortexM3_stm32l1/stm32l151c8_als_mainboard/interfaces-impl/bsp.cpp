/***************************************************************************
 *   Copyright (C) 2012 by Terraneo Federico                               *
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *                                                                         *
 *   This program is distributed in the hope that it will be useful,       *
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of        *
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the         *
 *   GNU General Public License for more details.                          *
 *                                                                         *
 *   As a special exception, if other files instantiate templates or use   *
 *   macros or inline functions from this file, or you compile this file   *
 *   and link it with other works to produce a work based on this file,    *
 *   this file does not by itself cause the resulting work to be covered   *
 *   by the GNU General Public License. However the source code for this   *
 *   file must still be made available in accordance with the GNU General  *
 *   Public License. This exception does not invalidate any other reasons  *
 *   why a work based on this file might be covered by the GNU General     *
 *   Public License.                                                       *
 *                                                                         *
 *   You should have received a copy of the GNU General Public License     *
 *   along with this program; if not, see <http://www.gnu.org/licenses/>   *
 ***************************************************************************/ 

/***********************************************************************
* bsp.cpp Part of the Miosix Embedded OS.
* Board support package, this file initializes hardware.
************************************************************************/


#include <cstdlib>
#include <inttypes.h>
#include "interfaces/bsp.h"
#include "kernel/kernel.h"
#include "kernel/sync.h"
#include "interfaces/delays.h"
#include "interfaces/portability.h"
#include "interfaces/arch_registers.h"
#include "config/miosix_settings.h"
#include "kernel/logging.h"
#include "console-impl.h"

namespace miosix {

//
// Initialization
//

void IRQbspInit()
{
    //Enable all GPIOs
    RCC->AHBENR |= RCC_AHBENR_GPIOAEN
                 | RCC_AHBENR_GPIOBEN
                 | RCC_AHBENR_GPIOCEN
                 | RCC_AHBENR_GPIOHEN;
    
    //Port config (H=high, L=low, PU=pullup, PD=pulldown)
	//  |  PORTA       |  PORTB      |  PORTC  |  PORTH  |
	//--+--------------+-------------+---------+---------+
	// 0| IN           | IN PD       | -       | IN PD   |
	// 1| IN PD        | IN PD       | -       | IN PD   |
	// 2| IN           | IN PD       | -       | -       |
	// 3| IN PD        | IN PD       | -       | -       |
	// 4| OUT H  10MHz | IN PD       | -       | -       |
	// 5| AF5    10MHz | IN PD       | -       | -       |
	// 6| AF5    10MHz | IN PD       | -       | -       |
	// 7| AF5    10MHz | IN PD       | -       | -       |
	// 8| OUT L  10MHz | IN PD       | -       | -       |
	// 9| AF7   400KHz | IN PD       | -       | -       |
	//10| AF7PU 400KHz | IN PD       | -       | -       |
	//11| IN PD        | OUT L 10MHz | -       | -       |
	//12| IN PD        | OUT L 10MHz | -       | -       |
	//13| OUT L 400KHz | OUT L 10MHz | IN PD   | -       |
	//14| OUT H 400KHz | OUT L 10MHz | AF0     | -       |
	//15| OUT L 400KHz | OUT L 10MHz | AF0     | -       |
    
    GPIOA->OSPEEDR=0x0002aa00;
    GPIOB->OSPEEDR=0xaa800000;
    
    GPIOA->MODER=0x5429a900;
    GPIOB->MODER=0x55400000;
    GPIOC->MODER=0xa0000000;
    GPIOH->MODER=0x00000000;
    
    GPIOA->PUPDR=0x02900088;
    GPIOB->PUPDR=0x002aaaaa;
    GPIOC->PUPDR=0x08000000;
    GPIOH->PUPDR=0x0000000a;
    
    GPIOA->ODR=0x00004010;
    GPIOB->ODR=0x00000000;
    
    GPIOA->AFR[0]= 0 |  0<<4 |  0<<8 |  0<<12 |  0<<16 |  5<<20 |  5<<24 |  5<<28;
    GPIOA->AFR[1]= 0 |  7<<4 |  7<<8 |  0<<12 |  0<<16 |  0<<20 |  0<<24 |  0<<28;
    GPIOB->AFR[0]= 0 |  0<<4 |  0<<8 |  0<<12 |  0<<16 |  0<<20 |  0<<24 |  0<<28;
    GPIOB->AFR[1]= 0 |  0<<4 |  0<<8 |  0<<12 |  0<<16 |  0<<20 |  0<<24 |  0<<28;
    GPIOC->AFR[0]= 0 |  0<<4 |  0<<8 |  0<<12 |  0<<16 |  0<<20 |  0<<24 |  0<<28;
    GPIOC->AFR[1]= 0 |  0<<4 |  0<<8 |  0<<12 |  0<<16 |  0<<20 |  0<<24 |  0<<28;
       
    ledOn();
    delayMs(100);
    ledOff();
    
    #ifndef STDOUT_REDIRECTED_TO_DCC
    IRQstm32f2serialPortInit();
    #endif //STDOUT_REDIRECTED_TO_DCC
}

void bspInit2()
{
    //Nothing to do
}

//
// Shutdown and reboot
//

void shutdown()
{
    disableInterrupts();
    for(;;) ;
}

void reboot()
{
    while(!Console::txComplete()) ;
    disableInterrupts();
    miosix_private::IRQsystemReboot();
}

};//namespace miosix
