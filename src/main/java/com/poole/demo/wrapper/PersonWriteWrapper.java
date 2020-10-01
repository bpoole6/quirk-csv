package com.poole.demo.wrapper;

import com.poole.csv.wrappers.write.WriteWrapper;

public class PersonWriteWrapper implements WriteWrapper<Person> {
    @Override
    public String apply(Person p) {
        return p.getId()+"";
    }
}
