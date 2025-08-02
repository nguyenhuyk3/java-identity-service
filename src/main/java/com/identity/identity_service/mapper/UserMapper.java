package com.identity.identity_service.mapper;

import com.identity.identity_service.dto.request.UserCreationRequest;
import com.identity.identity_service.dto.request.UserUpdateRequest;
import com.identity.identity_service.dto.response.UserResponse;
import com.identity.identity_service.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
/*
    Annotation @Mapper(componentModel = "spring") giúp MapStruct sinh ra 1 bean Spring cho interface UserMapper.
    Nhờ đó, bạn có thể inject UserMapper bằng @Autowired hoặc thông qua constructor trong service.
*/
public interface UserMapper {
    User toUser(UserCreationRequest req);
    UserResponse toUserResponse(User user);
    /*
        @MappingTarget là một annotation của MapStruct, dùng để cập nhật dữ liệu cho một đối tượng đã tồn tại (thay vì tạo mới).
        Mục đích của @MappingTarget:
            - Khi bạn muốn MapStruct copy dữ liệu từ một object nguồn (source)
        sang object đích (target) đã tồn tại, bạn dùng @MappingTarget để đánh dấu đối tượng đích.
    */
    void updateUser(@MappingTarget User user, UserUpdateRequest req);
}
