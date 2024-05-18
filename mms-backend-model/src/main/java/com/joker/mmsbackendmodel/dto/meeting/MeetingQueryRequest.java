package com.joker.mmsbackendmodel.dto.meeting;



import lombok.Data;
import lombok.EqualsAndHashCode;
import com.joker.mmsbackendcommon.common.PageRequest;

import java.io.Serializable;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class MeetingQueryRequest extends PageRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * name
     */
    private String  meetingName;

    /**
     * 容纳人数
     */
    private Long  meetingCapacity;

    /**
     * 会议室地点
     */
    private String  meetingLocation;

    /**
     * 会议室状态
     */
    private Boolean  meetingStatus;

    /**
     * 附带设备
     */
    private List<String> meetingTags;

    private static final long serialVersionUID = 1L;
}
