<?php

namespace App\Controller;

use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Attribute\Route;
use App\Entity\Client;
use App\Form\ClientType;
use App\Utils\Encrypt;
use Doctrine\ORM\EntityManagerInterface;
use App\Repository\ClientRepository;
use Symfony\Component\Security\Core\Authentication\Token\UsernamePasswordToken;
use Symfony\Component\Security\Core\Security;
use Symfony\Component\Security\Core\Authentication\Token\Storage\TokenStorageInterface;
use App\Utils\MailSender;
use App\Utils\GoogleAuthenticator;

final class SignInController extends AbstractController
{
    private $encrypt;
    private $clientRepository;
    private $tokenStorage;
    public function __construct(Encrypt $encrypt, ClientRepository $clientRepository, TokenStorageInterface $tokenStorage)
    {
        $this->encrypt = $encrypt;
        $this->clientRepository = $clientRepository;
        $this->tokenStorage = $tokenStorage;
    }

    #[Route('/signIn', name: 'app_sign_in')]
    public function signIn(Request $request, EntityManagerInterface $em): Response
    {
        $client = new Client();
        $client1 = new Client();
        $form = $this->createForm(ClientType::class, $client);
        $form1 = $this->createForm(ClientType::class, $client1);
        return $this->render('sign_in/sign.html.twig', [
            'form' => $form->createView(),
            'form1' => $form1->createView(),
            'login' => true,
        ]);
    }

    
    

    #[Route('/login', name: 'app_login')]
    public function login(Request $request): Response
    {
        $client = new Client();
        $form = $this->createForm(ClientType::class, $client);
        $form->handleRequest($request);
    

        if ($form->isSubmitted() && $form->get('email')->isValid() && $form->get('mdp')->isValid()) {
            $email =$form->get('email')->getData();
            $mdp = $form->get('mdp')->getData();
        
           
    
            
            if ($this->clientRepository->clientExiste($email, $mdp)) {

                $clientFromDb = $this->clientRepository->trouverParEmail($client->getEmail());


                
                $token = new UsernamePasswordToken($clientFromDb, "main", $clientFromDb->getRoles());
                
        
                
                // Stocker le token dans le TokenStorage
                $this->tokenStorage->setToken($token);
                
                return $this->redirectToRoute("app_profile");
            } else {
                return $this->render('profile/front.html.twig');
               
            }
        }

        return $this->render('sign_in/sign.html.twig', [
            'form' => $form->createView(),
            'form1' => $form->createView(),
            'login' => false,
        ]);
    }

   

    #[Route('/forgot_password_request', name: 'app_forgot_password_request')]
    public function forgotPassword(Request $request, MailSender $mailSender): Response
    {
        if ($request->isMethod('POST')) {
                $email = $request->request->get('email');

            if (!$email) {
                return $this->render('sign_in/forgotPassword.html.twig', [
                    'error' => 'Veuillez entrer une adresse email.',
                ]);
            }

            $client = $this->clientRepository->trouverParEmail($email);

            if (!$client) {
                return $this->render('sign_in/forgotPassword.html.twig', [
                    'error' => 'Aucun compte trouvé avec cette adresse email.',
                ]);
            }

            $userId = $client->getId();
            $encryptedId = $this->encrypt->encryptAES($userId);
            $resetLink = 'http://localhost/resetPassword.php?id=' . urlencode($encryptedId);

            $sujet = 'Réinitialisation de votre mot de passe';
            $contenu = "Cliquez sur le lien pour réinitialiser votre mot de passe : \n" . $resetLink;

            $envoiReussi = $mailSender->envoyerEmail($email, $sujet, $contenu);

            if ($envoiReussi) {
                return $this->render('sign_in/forgotPassword.html.twig', [
                    'success' => 'Un email de réinitialisation a été envoyé à votre adresse.',
                ]);
            } else {
                return $this->render('sign_in/forgotPassword.html.twig', [
                    'error' => 'Erreur lors de l\'envoi de l\'email.',
                ]);
            }
           
        }
        

        return $this->render('sign_in/forgotPassword.html.twig');
    }

    #[Route('/register', name: 'app_register')]
    public function register(Request $request, EntityManagerInterface $em)
    {
        $client = new Client();
        $client1 = new Client();
        $form1 = $this->createForm(ClientType::class, $client1);
        $form = $this->createForm(ClientType::class, $client);
        $form->handleRequest($request);
    
        if ($form->isSubmitted() && $form->isValid()) {
            $email = $form->get('email')->getData();
            if ($this->clientRepository->emailExiste($email)) {
                return $this->render('sign_in/sign.html.twig', [
                    'form' => $form->createView(),
                    'form1' => $form->createView(),
                    'login' => true,
                    'error' => 'L\'adresse email existe déjà.',
                ]);
            }
            $dateNaissance = $form->get('dateNaissance')->getData();
            if ($dateNaissance instanceof \DateTime) {
                $client->setDateNaissance($dateNaissance->format('Y-m-d'));
            }
            $imageFile = $form->get('image')->getData();
            if ($imageFile) {
                $newFilename = uniqid().'.'.$imageFile->guessExtension();
                $imageFile->move('C:/xampp/htdocs/images/', $newFilename);
                $client->setImage('C:/xampp/htdocs/images/' . $newFilename);
            }
            else {
                $client->setImage('C:/xampp/htdocs/images/logo.png');
            }
            
            $encryptedPassword = $this->encrypt->encryptAES($form->get('mdp')->getData());
            $client->setMdp($encryptedPassword);
            $client->setId(0);

            $em->persist($client);
            $em->flush();

            
        }

        

        return $this->render('sign_in/sign.html.twig', [
            'form' => $form->createView(),
            'form1' => $form->createView(),
            'login' => false,
        ]);
    }

   


    

    #[Route('/check-token', name: 'check_token')]
    public function checkToken(TokenStorageInterface $tokenStorage, Security $security): Response
    {
        $token = $tokenStorage->getToken();
        
        if (!$token || !$token->getUser()) {
            return new Response('Aucun utilisateur connecté.');
        }

        return new Response('Utilisateur connecté : ' . $security->getUser()->getUserIdentifier());
    }


    #[Route('/google/login', name: 'app_google_login')]
    public function googleLogin(GoogleAuthenticator $googleAuthenticator): Response
    {
        return $this->redirect($googleAuthenticator->getAuthUrl());
    }

    #[Route('/google/callback', name: 'app_google_callback')]
    public function googleCallback(
        Request $request,
        GoogleAuthenticator $googleAuthenticator,
        ClientRepository $clientRepository,
        EntityManagerInterface $em
    ): Response {
        $code = $request->query->get('code');

        if (!$code) {
            $this->addFlash('error', 'Erreur lors de l\'authentification Google.');
            return $this->redirectToRoute('app_login');
        }

        $accessToken = $googleAuthenticator->getAccessToken($code);
        $googleUser = $googleAuthenticator->getUserInfo($accessToken);
        

        // Accès aux données depuis le tableau associatif
        $email = $googleUser['email'] ?? 'email inconnu';
        $nom = $googleUser['family_name'] ?? 'Nom inconnu';
        $prenom = $googleUser['given_name'] ?? 'Prénom inconnu';
        $image = $googleUser['picture'] ?? '/Styles/logo.png';

        // Sexe par défaut
        $sexe = 'Non spécifié';
        if (isset($googleUser['gender'])) {
            $sexe = $googleUser['gender'] === 'male' ? 'homme' : 'femme';
        }

        // Date de naissance par défaut
        $dateNaissance = '2004-01-10';
        if (!empty($googleUser['birthday'])) {
            $dateNaissance = $googleUser['birthday'];
        }



        $client = $clientRepository->findOneBy(['email' => $email]);

        if (!$client) {
            $client = new Client();
            $client->setNom($nom);
            $client->setPrenom($prenom);
            $client->setEmail($email);
            $client->setDateNaissance($dateNaissance);
            $client->setSexe($sexe);
            $client->setId(0);
            $client->setPoids(70);
            $client->setTaille(170);
            $client->setMdp($this->encrypt->encryptAES('Ahmed123')); 
            $client->setImage($image);

            $em->persist($client);
            $em->flush();
            
        }
        $clientFromDb = $this->clientRepository->trouverParEmail($client->getEmail());


                
        $token = new UsernamePasswordToken($clientFromDb, "main", $clientFromDb->getRoles());
        

        
        // Stocker le token dans le TokenStorage
        $this->tokenStorage->setToken($token);
        
        return $this->redirectToRoute("app_profile");


    }

    #[Route('/logout', name: 'app_logout')]
    public function logout(): void
    {
        // Symfony intercepte cette route automatiquement, pas besoin de code ici
        throw new \Exception('This method can be blank - it will be intercepted by the logout key on your firewall.');
    }



}
