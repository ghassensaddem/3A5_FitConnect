<?php

namespace App\Utils;


use Google\Client as GoogleClient;
use Symfony\Component\Routing\Generator\UrlGeneratorInterface;
use Google\Service\Oauth2;

class GoogleAuthenticator
{
    private GoogleClient $googleClient;

    public function __construct()
    {
        $this->googleClient = new GoogleClient ();
        $this->googleClient->setAuthConfig(__DIR__ . '/../../config/client_secret_84102474659-isd7315dqh7sm3c7elbtdjq5efi5n2pi.apps.googleusercontent.com.json');
        $this->googleClient->setRedirectUri('http://localhost:8000/google/callback');
        $this->googleClient->addScope([
            'openid',
            'profile',
            'email',
            'https://www.googleapis.com/auth/userinfo.profile',
            'https://www.googleapis.com/auth/user.birthday.read', // 🔥 Récupérer la date de naissance
            'https://www.googleapis.com/auth/user.gender.read'   // 🔥 Récupérer le sexe
        ]);
        
    }

    public function getAuthUrl(): string
    {
        return $this->googleClient->createAuthUrl();
    }

    public function getAccessToken(string $code)
    {
        return $this->googleClient->fetchAccessTokenWithAuthCode($code);
    }

    public function getUserInfo($accessToken)
    {
        $this->googleClient->setAccessToken($accessToken);
        $peopleService = new \Google\Service\PeopleService($this->googleClient);

        // Récupérer les données utilisateur
        $person = $peopleService->people->get('people/me', [
            'personFields' => 'names,emailAddresses,photos,genders,birthdays'
        ]);
            // Extraire les données souhaitées
        $name = $person->getNames()[0]->getDisplayName();
        $firstName = $person->getNames()[0]->getGivenName();
        $lastName = $person->getNames()[0]->getFamilyName();
        $email = $person->getEmailAddresses()[0]->getValue();
        $photoUrl = $person->getPhotos()[0]->getUrl();

        $gender = null;
        if ($person->getGenders()) {
            $gender = $person->getGenders()[0]->getValue();
        }

        $birthday = null;
        if ($person->getBirthdays()) {
            $bday = $person->getBirthdays()[0]->getDate();
            $birthday = sprintf('%04d-%02d-%02d', $bday->getYear(), $bday->getMonth(), $bday->getDay());
        }

        // Retourner un tableau avec les infos utiles
        return [
            'name' => $name,
            'given_name' => $firstName,
            'family_name' => $lastName,
            'email' => $email,
            'picture' => $photoUrl,
            'gender' => $gender,
            'birthday' => $birthday,
        ];
        }
}
