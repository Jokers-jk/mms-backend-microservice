package com.joker.mmsbackendmodel.dto.meeting;

import lombok.Data;

import java.io.Serializable;

@Data
public class MeetingDeleteRequest implements Serializable {

    /**
     * id
     */
    private Long id;


    private static final long serialVersionUID = 1L;
}
