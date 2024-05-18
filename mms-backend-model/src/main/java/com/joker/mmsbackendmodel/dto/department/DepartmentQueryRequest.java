package com.joker.mmsbackendmodel.dto.department;


import lombok.Data;
import lombok.EqualsAndHashCode;
import com.joker.mmsbackendcommon.common.PageRequest;


import java.io.Serializable;



@EqualsAndHashCode(callSuper = true)
@Data
public class DepartmentQueryRequest extends PageRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * name
     */
    private String departmentName;

    private static final long serialVersionUID = 1L;

}
