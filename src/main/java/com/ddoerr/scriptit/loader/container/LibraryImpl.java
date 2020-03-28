package com.ddoerr.scriptit.loader.container;

import com.ddoerr.scriptit.api.libraries.Library;
import com.ddoerr.scriptit.api.libraries.Model;

public class LibraryImpl implements Library {
    private String name;
    private Model model;

    public LibraryImpl(String name, Model model) {
        this.name = name;
        this.model = model;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Model getModel() {
        return model;
    }
}
