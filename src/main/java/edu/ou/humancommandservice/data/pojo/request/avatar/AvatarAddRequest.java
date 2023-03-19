package edu.ou.humancommandservice.data.pojo.request.avatar;


import edu.ou.coreservice.data.pojo.request.base.IBaseRequest;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

@Data
public class AvatarAddRequest implements IBaseRequest {

    @NotNull
    private MultipartFile image;
}
