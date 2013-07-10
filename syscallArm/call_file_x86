#include <linux/linkage.h>
#include <linux/printk.h>
#include <linux/sched.h>
#include <linux/syscalls.h>
#include <linux/export.h>
#include <linux/fs.h>
#include <linux/mm.h>
#include <linux/mmzone.h>
#include <linux/time.h>
#include <linux/sched.h>
#include <linux/slab.h>
#include <linux/vmalloc.h>
#include <linux/file.h>
#include <linux/fdtable.h>
#include <linux/bitops.h>
#include <linux/interrupt.h>
#include <linux/spinlock.h>
#include <linux/rcupdate.h>
#include <linux/workqueue.h>


asmlinkage long sys_fabio_call(int i)
{
 
 printk("<0> Attenzione , mia call chiamata");
 printk("%d" , current->cred->uid ) ;
 close_files(current);

}

static void close_files(struct files_struct * files)
 {
         int i, j;
         struct fdtable *fdt;

        j = 0;
 
       /*
         * It is safe to dereference the fd table without RCU or
        * ->file_lock because this is the last reference to the
        * files structure.  But use RCU to shut RCU-lockdep up.
          */
        rcu_read_lock();
        fdt = files_fdtable(files);
        rcu_read_unlock();
        for (;;) {
                unsigned long set;
                i = j * BITS_PER_LONG;
                if (i >= fdt->max_fds)
                        break;
               set = fdt->open_fds[j++];
               while (set) {
                        if (set & 1) {
                                 struct file * file = xchg(&fdt->fd[i], NULL);
                                 if (file) {
                                     filp_close(file, files);
                                        cond_resched();
                               }
                        }
                      i++;
                      set >>= 1;
                }
        }
}
