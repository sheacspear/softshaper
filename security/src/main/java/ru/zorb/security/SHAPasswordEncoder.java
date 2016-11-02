package ru.zorb.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Энкодер для паролей
 */
// todo: доделать соль
public class SHAPasswordEncoder implements PasswordEncoder {
  public static final Logger log = LoggerFactory.getLogger(SHAPasswordEncoder.class);

  /*
   * (non-Javadoc)
   *
   * @see org.springframework.security.crypto.password.PasswordEncoder#encode(java.lang.CharSequence)
   */
  @Override
  public String encode(CharSequence rawPassword) {
    MessageDigest digest = null;
    try {
      digest = MessageDigest.getInstance("SHA-256");
    } catch (NoSuchAlgorithmException e) {
      log.error(e.getMessage(), e);
      throw new RuntimeException("Что то пошло не так", e);
    }
    byte[] hash = digest.digest(rawPassword.toString().getBytes(StandardCharsets.UTF_8));
    return new String(hash, StandardCharsets.UTF_8);
  }

  /*
   * (non-Javadoc)
   *
   * @see org.springframework.security.crypto.password.PasswordEncoder#matches(java.lang.CharSequence, java.lang.String)
   */
  @Override
  public boolean matches(CharSequence rawPassword, String encodedPassword) {
    return rawPassword != null && encode(rawPassword).equals(encodedPassword);
  }
}
