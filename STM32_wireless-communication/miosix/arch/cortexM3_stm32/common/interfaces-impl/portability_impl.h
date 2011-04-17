/***************************************************************************
 *   Copyright (C) 2010 by Terraneo Federico                               *
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
//Miosix kernel

#ifndef PORTABILITY_IMPL_H
#define PORTABILITY_IMPL_H

#include "CMSIS/stm32f10x.h"
#include "CMSIS/core_cm3.h"
#include "CMSIS/system_stm32f10x.h"
#include "config/miosix_settings.h"

/**
 * \addtogroup Drivers
 * \{
 */

/*
 * This pointer is used by the kernel, and should not be used by end users.
 * this is a pointer to a location where to store the thread's registers during
 * context switch. It requires C linkage to be used inside asm statement.
 * Registers are saved in the following order:
 * *ctxsave+32 --> r11
 * *ctxsave+28 --> r10
 * *ctxsave+24 --> r9
 * *ctxsave+20 --> r8
 * *ctxsave+16 --> r7
 * *ctxsave+12 --> r6
 * *ctxsave+8  --> r5
 * *ctxsave+4  --> r4
 * *ctxsave+0  --> psp
 */
extern "C" {
extern volatile unsigned int *ctxsave;
}

/**
 * \internal
 * \def saveContext()
 * Save context from an interrupt<br>
 * Must be the first line of an IRQ where a context switch can happen.
 * The IRQ must be "naked" to prevent the compiler from generating context save.
 */
#define saveContext()                                                        \
{                                                                             \
    asm volatile("stmdb sp!, {lr}        \n\t" /*save lr on MAIN stack*/      \
                 "mrs   r1,  psp         \n\t" /*get PROCESS stack pointer*/  \
                 "ldr   r0,  =ctxsave    \n\t" /*get current context*/        \
                 "ldr   r0,  [r0]        \n\t"                                \
                 "stmia r0,  {r1,r4-r11} \n\t" /*save PROCESS sp + r4-r11*/   \
                 );                                                           \
}

/**
 * \def restoreContext()
 * Restore context in an IRQ where saveContext() is used. Must be the last line
 * of an IRQ where a context switch can happen. The IRQ must be "naked" to
 * prevent the compiler from generating context restore.
 */
#define restoreContext()                                                     \
{                                                                             \
    asm volatile("ldr   r0,  =ctxsave    \n\t" /*get current context*/        \
                 "ldr   r0,  [r0]        \n\t"                                \
                 "ldmia r0,  {r1,r4-r11} \n\t" /*restore r4-r11 + r1=psp*/    \
                 "msr   psp, r1          \n\t" /*restore PROCESS sp*/         \
                 "ldmia sp!, {pc}        \n\t" /*return*/                     \
                 );                                                           \
}

/**
 * \}
 */

namespace miosix_private {
    
/**
 * \addtogroup Drivers
 * \{
 */

inline void doYield()
{
    asm volatile("svc 0");
}

inline void doDisableInterrupts()
{
    // Documentation says __disable_irq() disables all interrupts with
    // configurable priority, so also SysTick and SVC.
    // No need to disable faults with __disable_fault_irq()
    __disable_irq();
}

inline void doEnableInterrupts()
{
    __enable_irq();
}

inline bool checkAreInterruptsEnabled()
{
    register int i;
    asm volatile("mrs   %0, primask    \n\t":"=r"(i));
    if(i!=0) return false;
    return true;
}

/**
 * \}
 */

}; //namespace miosix_private

#endif //PORTABILITY_IMPL_H
