package com.identity.identity_service.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.identity.identity_service.dto.requests.UserCreationRequest;
import com.identity.identity_service.dto.requests.UserUpdateRequest;
import com.identity.identity_service.dto.responses.UserResponse;
import com.identity.identity_service.entities.User;

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

    @Mapping(target = "roles", ignore = true)
    void updateUser(@MappingTarget User user, UserUpdateRequest req);
}
