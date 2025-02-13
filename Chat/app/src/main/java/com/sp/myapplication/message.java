package com.sp.myapplication;

import java.util.Date;

public class message {
    private String text;
    private boolean isSentByUser;
    private Date timestamp;
    private String avatarUrl;

        public message(String text, boolean isSentByUser, Date timestamp, String avatarUrl) {
            this.text = text;
            this.isSentByUser = isSentByUser;
            this.timestamp = timestamp;
            this.avatarUrl = avatarUrl;
        }

        public String getText() {
            return text;
        }

        public boolean isSentByUser() {
            return isSentByUser;
        }

        public Date getTimestamp() {
            return timestamp;
        }

        public String getAvatarUrl() {
            return avatarUrl;
        }
    }
