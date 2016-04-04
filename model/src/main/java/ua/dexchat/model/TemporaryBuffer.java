package ua.dexchat.model;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by dexter on 01.04.16.
 */
public class TemporaryBuffer {

    @OneToMany(mappedBy = "buffer", cascade = {CascadeType.PERSIST}, fetch = FetchType.EAGER)
    private Queue<Message> messages = new LinkedList<>();

}
