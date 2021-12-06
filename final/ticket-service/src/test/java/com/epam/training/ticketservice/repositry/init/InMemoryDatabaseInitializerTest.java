package com.epam.training.ticketservice.repositry.init;

import com.epam.training.ticketservice.core.configuration.InMemoryDatabaseInitializer;
import com.epam.training.ticketservice.core.user.persistence.entity.User;
import com.epam.training.ticketservice.core.user.persistence.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.env.Environment;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

class InMemoryDatabaseInitializerTest {

    private static final User ADMIN_ACCOUNT = new User("admin", "admin", true);

    private InMemoryDatabaseInitializer underTest;

    @Mock
    private UserRepository accountRepository;

    @Mock
    private Environment environment;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        underTest = new InMemoryDatabaseInitializer(accountRepository, environment);
    }

    @Test
    void testInitProductsShouldSaveAdminWhenCiProfileIsActive() {
        // Given
        final InMemoryDatabaseInitializer accountInitializer = spy(underTest);
        doReturn(true).when(accountInitializer).isProfileCiActive();

        // When
        accountInitializer.initProducts();

        // Then
        verify(accountRepository).save(any(User.class));
    }

    @Test
    void testInitProductsShouldSaveAdminWhenCiProfileIsNotActiveButItIsNotInitializedYet() {
        // Given
        final InMemoryDatabaseInitializer accountInitializer = spy(underTest);
        doReturn(false).when(accountInitializer).isProfileCiActive();
        given(accountRepository.findById(ADMIN_ACCOUNT.getUsername())).willReturn(Optional.empty());

        // When
        accountInitializer.initProducts();

        // Then
        verify(accountRepository).save(any(User.class));
    }

    @Test
    void testInitProductsShouldDoNothingWhenCiProfileIsNotActiveAndAlreadyInitialized() {
        // Given
        final InMemoryDatabaseInitializer accountInitializer = spy(underTest);
        doReturn(false).when(accountInitializer).isProfileCiActive();
        given(accountRepository.findById(ADMIN_ACCOUNT.getUsername())).willReturn(Optional.of(ADMIN_ACCOUNT));

        // When
        accountInitializer.initProducts();

        // Then
        verify(accountRepository, never()).save(any(User.class));
    }

    @Test
    void testIsProfileCiActiveShouldReturnTrueWhenCiIsActive() {
        // Given
        given(environment.getActiveProfiles()).willReturn(new String[]{"ci"});

        // When
        final boolean actual = underTest.isProfileCiActive();

        // Then
        assertTrue(actual);
    }

    @Test
    void testIsProfileCiActiveShouldReturnFalseWhenCiIsNotActive() {
        // Given
        given(environment.getActiveProfiles()).willReturn(new String[]{});

        // When
        final boolean actual = underTest.isProfileCiActive();

        // Then
        assertFalse(actual);
    }
}
