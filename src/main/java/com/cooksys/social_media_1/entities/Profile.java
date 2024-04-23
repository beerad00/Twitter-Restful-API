package com.cooksys.social_media_1.entities;

import jakarta.annotation.Nonnull;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class Profile {

    String firstName;
    String lastName;
    @Nonnull
    String email;
    String phone;
}
