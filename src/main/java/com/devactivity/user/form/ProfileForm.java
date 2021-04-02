package com.devactivity.user.form;

import com.github.dozermapper.core.Mapping;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

@Data
public class ProfileForm {

    @Length(max = 100)
    @Mapping("bio")
    private String bio;

    @URL
    @Mapping("rssUrl")
    private String rssUrl;

}
