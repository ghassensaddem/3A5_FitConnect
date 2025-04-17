<?php

namespace App\Utils;

use Symfony\Component\PasswordHasher\PasswordHasherInterface;

class Encrypt implements PasswordHasherInterface
{
    const SECRET_KEY = "1234567890123456";
    const API_KEY = "asjkkpssbhf";

    // Fonction de chiffrement AES en PHP
    public function encryptAES($data)
    {
        $iv = substr(self::SECRET_KEY, 0, 16); // IV (doit être de 16 octets)
        return base64_encode(openssl_encrypt($data, "AES-128-CBC", self::SECRET_KEY, OPENSSL_RAW_DATA, $iv));
    }

    // Fonction de déchiffrement AES en PHP
    public function decryptAES($data)
    {
        $iv = substr(self::SECRET_KEY, 0, 16); // IV doit être de 16 octets
        return openssl_decrypt(base64_decode($data), "AES-128-CBC", self::SECRET_KEY, OPENSSL_RAW_DATA, $iv);
    }

    // Implémentation de hash() pour Symfony
    public function hash(string $plainPassword, string $salt = null): string
    {
        return $this->encryptAES($plainPassword); // Utilise le chiffrement AES comme "hash"
    }

    // Implémentation de verify() pour Symfony
    public function verify(string $hashedPassword, string $plainPassword, string $salt = null): bool
    {
        return $hashedPassword === $this->encryptAES($plainPassword);
    }

    // Implémentation de needsRehash() (peu utile ici, mais nécessaire)
    public function needsRehash(string $hashedPassword): bool
    {
        return false; // On ne change jamais la méthode de hachage
    }
}
