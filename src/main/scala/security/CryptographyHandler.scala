package security

import domain.Base64
import security.Security.{Encrypted, Plain}

object Security {
  type Plain = String
  type Encrypted = String
}

trait Encrypter {
  def encrypt(value: Plain): Encrypted
}

trait Decrypter {
  def decrypt(value: Encrypted): Plain
}

trait CryptographyHandler extends Encrypter with Decrypter

class FakeCryptographyHandler extends CryptographyHandler {
  override def encrypt(value: Plain): Encrypted = Base64.encode(value).base64AsString
  override def decrypt(value: Encrypted): Plain = Base64(value).unsafeDecoded
}
