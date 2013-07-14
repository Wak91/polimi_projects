/***************************************************************************
 *   Copyright (C) 2013 by Terraneo Federico                               *
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

#include "mountpointfs.h"

using namespace std;

namespace miosix {

//
// class DevFs
//

int MountpointFs::open(intrusive_ref_ptr<FileBase>& file, StringPart& name,
        int flags, int mode)
{
    return -EACCES; //MountpointFs does not support files, only directories
}

int MountpointFs::lstat(StringPart& name, struct stat *pstat)
{
    Lock<FastMutex> l(mutex);
    map<StringPart,int>::iterator it=dirs.find(name);
    if(it==dirs.end()) return -ENOENT;
    memset(pstat,0,sizeof(struct stat));
    pstat->st_ino=it->second;
    pstat->st_mode=S_IFDIR | 0755; //drwxr-xr-x
    pstat->st_nlink=1;
    pstat->st_blksize=512;
    return 0;
}

int MountpointFs::mkdir(StringPart& name, int mode)
{
    for(unsigned int i=1;i<name.length();i++)
        if(name[i]=='/')
            return -EACCES; //MountpointFs does not support subdirectories
    Lock<FastMutex> l(mutex);
    if(dirs.insert(make_pair(name,inodeCount)).second==false) return -EEXIST;
    inodeCount++;
    return 0;
}

} //namespace miosix
