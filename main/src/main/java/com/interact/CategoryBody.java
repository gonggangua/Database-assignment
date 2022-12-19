package com.interact;

import java.util.ArrayList;

public class CategoryBody {
    public String category_name;
    public ArrayList<ChannelBody> channels;

    public CategoryBody(String category_name) {
        this.category_name = category_name;
        this.channels = new ArrayList<>();
    }

    public void addChannel(ChannelBody channel) {
        channels.add(channel);
    }
}
