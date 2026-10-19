import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class MessageTest {

    private static final String R1 = "+27718693002";
    private static final String M1 = "Hi Mike, can you join us for dinner tonight?";
    private static final String R2 = "08575975889";
    private static final String M2 = "Hi Keegan, did you receive the payment?";

    @BeforeEach
    void reset() {
        Message.clearSentMessages();
    }

    // ---- message length ----
    @Test
    void messageLength_success() {
        assertEquals("Message ready to send.", Message.checkMessageLength(M1));
    }

    @Test
    void messageLength_failure() {
        String tooLong = "a".repeat(260);
        assertEquals("Message exceeds 250 characters by 10; please reduce the size.",
                Message.checkMessageLength(tooLong));
    }

    // ---- recipient number ----
    @Test
    void recipient_success() {
        assertEquals("Cell phone number successfully captured.", Message.checkRecipientCell(R1));
    }

    @Test
    void recipient_failure() {
        assertEquals("Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.",
                Message.checkRecipientCell(R2));
    }

    // ---- message hash ----
    @Test
    void hash_testCase1() {
        Message m = new Message("0012345678", 0, R1, M1);
        assertEquals("00:0:HITONIGHT", m.createMessageHash());
    }

    @Test
    void hash_remainingMessagesInLoop() {
        String[] ids = {"0012345678", "1234567890"};
        int[] numbers = {0, 1};
        String[] texts = {M1, M2};
        String[] expected = {"00:0:HITONIGHT", "12:1:HIPAYMENT"};
        for (int i = 0; i < texts.length; i++) {
            Message m = new Message(ids[i], numbers[i], R1, texts[i]);
            assertEquals(expected[i], m.createMessageHash());
        }
    }

    @Test
    void hash_singleWordMessage() {
        assertEquals("00:0:THANKSTHANKS", new Message("0012345678", 0, R1, "Thanks").createMessageHash());
    }

    // ---- message ID ----
    @Test
    void messageId_isCreated() {
        Message m = new Message(0, R1, M1);
        assertEquals(10, m.getMessageId().length());
        assertTrue(m.checkMessageID());
        assertEquals("Message ID generated: " + m.getMessageId(), m.getMessageIdMessage());
    }

    @Test
    void messageId_tooLongFails() {
        assertFalse(new Message("12345678901", 0, R1, M1).checkMessageID());
    }

    // ---- send / disregard / store ----
    @Test
    void sentMessage_send() {
        assertEquals("Message successfully sent.", new Message(0, R1, M1).sentMessage(Message.SEND));
    }

    @Test
    void sentMessage_disregard() {
        assertEquals("Press 0 to delete the message.", new Message(1, R2, M2).sentMessage(Message.DISREGARD));
    }

    @Test
    void sentMessage_store(@TempDir Path dir) throws Exception {
        Path file = dir.resolve("messages.json");
        Message m = new Message(0, R1, M1);
        m.storeMessage(file);
        assertTrue(Files.exists(file));
        List<Message> stored = Message.readStoredMessages(file);
        assertEquals(1, stored.size());
        assertEquals(M1, stored.get(0).getText());
    }

    // ---- totals & printing ----
    @Test
    void totalMessages_countsOnlySent() {
        new Message(0, R1, M1).sentMessage(Message.SEND);     // sent
        new Message(1, R2, M2).sentMessage(Message.DISREGARD); // discarded
        assertEquals(1, Message.returnTotalMessages());
    }

    @Test
    void printMessages_listsSentMessages() {
        Message m = new Message("0012345678", 0, R1, M1);
        m.sentMessage(Message.SEND);
        assertEquals("Message ID: 0012345678\nMessage Hash: 00:0:HITONIGHT\nRecipient: " + R1
                + "\nMessage: " + M1, Message.printMessages());
    }
}