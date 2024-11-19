package com.bitesaitzz.notifications_service.models;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class MailMessage {

    @JsonCreator
    public MailMessage(@JsonProperty("mail") String mail,
                       @JsonProperty("subject") String subject,
                       @JsonProperty("text") String text) {
        this.mail = mail;
        this.subject = subject;
        this.text = text;
    }
    private String mail;
    private String subject;
    private String text;


}
