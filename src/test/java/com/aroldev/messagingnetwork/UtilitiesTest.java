//package com.aroldev.messagingnetwork;
//
//import com.aroldev.messagingnetwork.Utils.Utilities;
//import com.aroldev.messagingnetwork.Enum.MessageType;
//import org.junit.jupiter.api.Test;
//import java.util.List;
//import java.util.ArrayList;
//import static org.junit.jupiter.api.Assertions.*;
//
//class UtilitiesTest {
//
//    @Test
//    void testFilterMessages() {
//        List<Message> messages = new ArrayList<>();
//        messages.add(new Message("Text1", System.currentTimeMillis(), MessageType.TEXT, "A"));
//        messages.add(new Message("Image1", System.currentTimeMillis(), MessageType.IMAGE, "B"));
//        messages.add(new Message("Text2", System.currentTimeMillis(), MessageType.TEXT, "C"));
//
//        List<Message> filtered = Utilities.filterMessages(messages);
//        assertEquals(2, filtered.size());
//        assertTrue(filtered.stream().allMatch(m -> m.getType() == MessageType.TEXT));
//    }
//}
