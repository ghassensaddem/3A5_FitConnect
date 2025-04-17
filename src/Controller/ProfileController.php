<?php

namespace App\Controller;

use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Attribute\Route;
use Symfony\Component\Security\Core\Security;
use Doctrine\ORM\EntityManagerInterface;
use App\Form\ClientType;
use App\Form\ProfileType;
use App\Entity\Client;
use Symfony\Component\HttpFoundation\Request;
use App\Repository\ClientRepository;
use Symfony\Component\Security\Core\Authentication\Token\UsernamePasswordToken;
use Symfony\Component\Security\Core\Authentication\Token\Storage\TokenStorageInterface;

final class ProfileController extends AbstractController
{
    private $clientRepository;
    private $tokenStorage;
    public function __construct(ClientRepository $clientRepository, TokenStorageInterface $tokenStorage)
    {
        $this->tokenStorage = $tokenStorage;
        $this->clientRepository = $clientRepository;
    }

    #[Route('/profile', name: 'app_profile')]
    public function index(Request $request,Security $security): Response
    {
        $user = $security->getUser(); // Récupère l'utilisateur connecté

        if (!$user) {
            throw $this->createAccessDeniedException('Vous devez être connecté pour accéder à cette page.');
        }
        if (!$user instanceof Client) {
            throw new \LogicException('Utilisateur invalide.');
        }
        $form = $this->createForm(ProfileType::class, $user);
        $form->handleRequest($request);

        


        if ($form->isSubmitted() &&  $form->get('nom')->isValid() && $form->get('prenom')->isValid() && $form->get('email')->isValid()) { 
            $client  = new Client();
            $client->setId($user->getId());
            $client->setNom($form->get('nom')->getData());
            $client->setPrenom($form->get('prenom')->getData());
            $client->setEmail($form->get('email')->getData());
            $client->setPassword('');

            if ($form->get('mdp')->getData() !== null) {
                $client->setPassword($form->get('mdp')->getData());
            }
            $this->clientRepository->updateClientInfo($client);


            
            $reloadedUser = $this->clientRepository->trouverParEmail($client->getEmail());

            $token = new UsernamePasswordToken($reloadedUser, 'main', $reloadedUser->getRoles());
            $this->tokenStorage->setToken($token);
            $this->container->get('security.token_storage')->setToken($token);

            return $this->redirectToRoute('app_profile');
        }

        return $this->render('profile/front.html.twig', [
            'client' => $user, // Envoie le client connecté au template
            'form' => $form->createView(),
        ]);
    }


    #[Route('/update_profile', name: 'update_profile')]
    public function update(): Response
    {
        return $this->render('profile/profile.html.twig', [
            'controller_name' => 'ProfileController',
        ]);
    }



    
    #[Route('/changeImage', name: 'changeImage', methods: ['POST'])]
    public function changeImage(Request $request, Security $security, EntityManagerInterface $em): Response
    {
        $user = $security->getUser();
        $client = new Client();
        
       
        

        if (!$user instanceof Client) {
            throw new \LogicException('Utilisateur invalide.');
        }
        
        $client->setId($user->getId());

        $imageFile = $request->files->get('image');

        if ($imageFile) {
            $newFilename = uniqid() . '.' . $imageFile->guessExtension();

            try {
                $imageFile->move('C:/xampp/htdocs/images/', $newFilename);
                $client->setImage('C:/xampp/htdocs/images/' . $newFilename);
                $this->clientRepository->updateImage($client);

            } catch (\Exception $e) {
                $this->addFlash('error', 'Une erreur est survenue lors du téléchargement de l\'image.');
            }
        }
        

        return $this->redirectToRoute('app_profile');
    }




}
