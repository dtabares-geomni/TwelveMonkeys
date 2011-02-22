/*
 * Copyright (c) 2009, Harald Kuhr
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name "TwelveMonkeys" nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.twelvemonkeys.imageio.metadata;

import com.twelvemonkeys.lang.Validate;

import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * AbstractEntry
 *
 * @author <a href="mailto:harald.kuhr@gmail.com">Harald Kuhr</a>
 * @author last modified by $Author: haraldk$
 * @version $Id: AbstractEntry.java,v 1.0 Nov 12, 2009 12:43:13 AM haraldk Exp$
 */
public abstract class AbstractEntry implements Entry {

    private final Object identifier;
    private final Object value; // TODO: Might need to be mutable..

    protected AbstractEntry(final Object identifier, final Object value) {
        Validate.notNull(identifier, "identifier");

        this.identifier = identifier;
        this.value = value;
    }

    public final Object getIdentifier() {
        return identifier;
    }

    /**
     * Returns {@code null}, meaning unknown or undefined.
     *
     * @return {@code null}.
     */
    public String getFieldName() {
        return null;
    }

    public Object getValue() {
        return value;
    }

    public String getValueAsString() {
        if (valueCount() > 1) {
            if (valueCount() < 16) {
                Class<?> type = value.getClass().getComponentType();

                if (type.isPrimitive()) {
                    if (type.equals(boolean.class)) {
                        return Arrays.toString((boolean[]) value);
                    }
                    else if (type.equals(byte.class)) {
                        return Arrays.toString((byte[]) value);
                    }
                    else if (type.equals(char.class)) {
                        return new String((char[]) value);
                    }
                    else if (type.equals(double.class)) {
                        return Arrays.toString((double[]) value);
                    }
                    else if (type.equals(float.class)) {
                        return Arrays.toString((float[]) value);
                    }
                    else if (type.equals(int.class)) {
                        return Arrays.toString((int[]) value);
                    }
                    else if (type.equals(long.class)) {
                        return Arrays.toString((long[]) value);
                    }
                    else if (type.equals(short.class)) {
                        return Arrays.toString((short[]) value);
                    }
                    // Fall through should never happen
                }
                else {
                    return Arrays.toString((Object[]) value);
                }
            }
            
            return String.valueOf(value) + " ("  + valueCount() + ")";
        }

        return String.valueOf(value);
    }

    public String getTypeName() {
        if (value == null) {
            return null;
        }

        return value.getClass().getSimpleName();
    }

    public int valueCount() {
        // TODO: Collection support?
        if (value != null && value.getClass().isArray()) {
            return Array.getLength(value);
        }

        return 1;
    }


    /// Object


    @Override
    public int hashCode() {
        return identifier.hashCode() + 31 * value.hashCode();
    }

    @Override
    public boolean equals(final Object pOther) {
        if (this == pOther) {
            return true;
        }
        if (!(pOther instanceof AbstractEntry)) {
            return false;
        }

        AbstractEntry other = (AbstractEntry) pOther;
        
        return identifier.equals(other.identifier) && (
                value == null && other.value == null || value != null && value.equals(other.value)
        );
    }

    @Override
    public String toString() {
        String name = getFieldName();
        String nameStr = name != null ? "/" + name + "" : "";

        String type = getTypeName();
        String typeStr = type != null ? " (" + type + ")" : "";

        return String.format("%s%s: %s%s", getIdentifier(), nameStr, getValueAsString(), typeStr);
    }
}