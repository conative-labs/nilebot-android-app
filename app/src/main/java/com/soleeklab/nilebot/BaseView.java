package com.soleeklab.nilebot;

public interface BaseView<T extends BasePresenter> extends ParentView {
    void setPresenter(T presenter);

}