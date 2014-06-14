/*
 * Copyright (C) 2011-2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*
 * This file is auto-generated. DO NOT MODIFY!
 * The source Renderscript file: /home/degrigis/Scrivania/androBenchmark/src/filter.rs
 */
package app.androbenchmark;

import android.renderscript.*;
import android.content.res.Resources;

/**
 * @hide
 */
public class ScriptC_filter extends ScriptC {
    private static final String __rs_resource_name = "filter";
    // Constructor
    public  ScriptC_filter(RenderScript rs) {
        this(rs,
             rs.getApplicationContext().getResources(),
             rs.getApplicationContext().getResources().getIdentifier(
                 __rs_resource_name, "raw",
                 rs.getApplicationContext().getPackageName()));
    }

    public  ScriptC_filter(RenderScript rs, Resources resources, int id) {
        super(rs, resources, id);
        __U8_4 = Element.U8_4(rs);
    }

    private Element __U8_4;
    private FieldPacker __rs_fp_ALLOCATION;
    private FieldPacker __rs_fp_SCRIPT;
    private final static int mExportVarIdx_gIn = 0;
    private Allocation mExportVar_gIn;
    public synchronized void set_gIn(Allocation v) {
        setVar(mExportVarIdx_gIn, v);
        mExportVar_gIn = v;
    }

    public Allocation get_gIn() {
        return mExportVar_gIn;
    }

    private final static int mExportVarIdx_gOut = 1;
    private Allocation mExportVar_gOut;
    public synchronized void set_gOut(Allocation v) {
        setVar(mExportVarIdx_gOut, v);
        mExportVar_gOut = v;
    }

    public Allocation get_gOut() {
        return mExportVar_gOut;
    }

    private final static int mExportVarIdx_gScript = 2;
    private Script mExportVar_gScript;
    public synchronized void set_gScript(Script v) {
        setVar(mExportVarIdx_gScript, v);
        mExportVar_gScript = v;
    }

    public Script get_gScript() {
        return mExportVar_gScript;
    }

    private final static int mExportFuncIdx_filter = 0;
    public void invoke_filter() {
        invoke(mExportFuncIdx_filter);
    }

}

