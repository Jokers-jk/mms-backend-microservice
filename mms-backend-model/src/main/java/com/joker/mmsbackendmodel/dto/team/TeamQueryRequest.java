package com.joker.mmsbackendmodel.dto.team;


import lombok.Data;
import lombok.EqualsAndHashCode;
import com.joker.mmsbackendcommon.common.PageRequest;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
public class TeamQueryRequest  extends PageRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * name
     */
    private String teamName;

    /**
     * 所属部门
     */
    private Long departmentId;


    private static final long serialVersionUID = 1L;
}
