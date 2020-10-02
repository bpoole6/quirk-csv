package com.quirk.demo.wrapper;

import com.quirk.csv.wrappers.write.WriteWrapper;

public class PersonWriteWrapper implements WriteWrapper<Person> {
    @Override
    public String apply(Person p) {
        return p.getId()+"";
    }
}
