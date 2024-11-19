package com.bitesaitzz.QuickPost;


import com.bitesaitzz.QuickPost.controllers.MessagesController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StorageTest {
    @Autowired
    MessagesController messagesController;


}
