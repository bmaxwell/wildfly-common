/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2017 Red Hat, Inc., and individual contributors
 * as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.wildfly.common.iteration;

import java.util.NoSuchElementException;

/**
 */
final class ConcatByteIterator extends ByteIterator {
    private final ByteIterator[] iterators;
    private long index = 0;

    ConcatByteIterator(final ByteIterator[] iterators) {
        this.iterators = iterators;
    }

    private int seekNext() {
        for (int i = 0; i < iterators.length; i ++) {
            if (iterators[i].hasNext()) {
                return i;
            }
        }
        return -1;
    }

    private int seekPrevious() {
        for (int i = iterators.length - 1; i >= 0; i --) {
            if (iterators[i].hasPrevious()) {
                return i;
            }
        }
        return -1;
    }

    public boolean hasNext() {
        return seekNext() != -1;
    }

    public boolean hasPrevious() {
        return seekPrevious() != -1;
    }

    public int next() throws NoSuchElementException {
        final int seek = seekNext();
        if (seek == -1) throw new NoSuchElementException();
        final int next = iterators[seek].next();
        index++;
        return next;
    }

    public int peekNext() throws NoSuchElementException {
        final int seek = seekNext();
        if (seek == -1) throw new NoSuchElementException();
        return iterators[seek].peekNext();
    }

    public int previous() throws NoSuchElementException {
        final int seek = seekPrevious();
        if (seek == -1) throw new NoSuchElementException();
        final int previous = iterators[seek].previous();
        index--;
        return previous;
    }

    public int peekPrevious() throws NoSuchElementException {
        final int seek = seekPrevious();
        if (seek == -1) throw new NoSuchElementException();
        return iterators[seek].peekPrevious();
    }

    public long getIndex() {
        return index;
    }
}
