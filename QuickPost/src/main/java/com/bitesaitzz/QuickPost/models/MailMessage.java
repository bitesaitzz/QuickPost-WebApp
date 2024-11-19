package com.bitesaitzz.QuickPost.models;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MailMessage {
    private String mail;
    private String subject;
    private String text;


}
