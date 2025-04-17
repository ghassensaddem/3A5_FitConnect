<?php

namespace App\Controller;

use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Attribute\Route;
use App\Repository\ClientRepository;
use App\Repository\CoachRepository;
use App\Repository\ActivityRepository;
use App\Repository\AdminRepository;
use App\Form\UpdateClientType;
use Symfony\Component\HttpFoundation\Request;
use App\Utils\Encrypt;
use App\Entity\Client;
use App\Entity\Coach;
use App\Entity\Admin;
use App\Form\AdminType;
use App\Form\CoachType;
use App\Form\UpdateAdminType;
use App\Form\UpdateCoachType;


final class UsersController extends AbstractController
{

    private $clientRepository;
    private $coachRepository;
    private $activityRepository;
    private $AdminRepository;
    private $encrypt;
    public function __construct(ClientRepository $clientRepository, CoachRepository $coachRepository, ActivityRepository $activityRepository, AdminRepository $AdminRepository, Encrypt $encrypt)
    {
        $this->clientRepository = $clientRepository;
        $this->coachRepository = $coachRepository;
        $this->activityRepository = $activityRepository;
        $this->AdminRepository = $AdminRepository;
        $this->encrypt = new Encrypt();
    }
    #[Route('/users', name: 'app_users')]
    public function index(Request $request): Response
    {
        $activityChoisie = $request->request->get('activity');
        if ($activityChoisie && $activityChoisie !== 'tous') {
            $coachs = $this->coachRepository->findBy(['specialite' => $activityChoisie]);
        } else {
            $coachs = $this->coachRepository->afficher();
        }

        $clients = $this->clientRepository->afficher();
        $activities = $this->activityRepository->afficherNomActivity();
        $totalClients = count($clients);
        $totalCoachs = count($coachs);
        return $this->render('users/listUsers.html.twig', [
            'clients' => $clients,
            'coachs' => $coachs,
            'activities' => $activities,
            'totalClients' => $totalClients,
            'totalCoachs' => $totalCoachs,
            'activityChoisie' => $activityChoisie,
        ]);
    }



    #[Route('/user/updateClient/{id}', name: 'updateClient')]
    public function updateClient(int $id,Request $request): Response
    {
        $client = $this->clientRepository->find($id);
        if (!$client) {
            throw $this->createNotFoundException('Utilisateur non trouvé.');
        }
        $form = $this->createForm(UpdateClientType::class, $client);
        $form->handleRequest($request);
        if ($form->isSubmitted()   && $form->get('nom')->isValid() && $form->get('prenom')->isValid() && $form->get('email')->isValid() && $form->get('poids')->isValid()  && $form->get('taille')->isValid()) {
            $dateNaissance = $form->get('dateNaissance')->getData();
            if ($dateNaissance instanceof \DateTime) {
                $client->setDateNaissance($dateNaissance->format('Y-m-d'));
            }
            if ($form->get('mdp')->getData() !== null) {
                $encryptedPassword = $this->encrypt->encryptAES($form->get('mdp')->getData());
                $client->setMdp($encryptedPassword);
            }
            
            
            $this->clientRepository->AdminUpdateClient($client);
            return $this->redirectToRoute('app_users');
        }
        return $this->render('users/UpdateClient.html.twig', [
            'form' => $form->createView(),
            'client' => $client,
        ]);
    }

    #[Route('/user/deleteClient/{id}', name: 'deleteClient')]
    public function deleteUser(int $id): Response
    {
        $client = $this->clientRepository->find($id);
        if ($client) {
            $this->clientRepository->supprimer($client);
            return $this->redirectToRoute('app_users');
        } else {
            throw $this->createNotFoundException('Utilisateur non trouvé.');
        }
    }

    #[Route('/user/updateClientImage/{id}', name: 'updateClientImage')]
    public function changeImage(int $id,Request $request): Response
    {
        $client = $this->clientRepository->find($id);
        if (!$client) {
            throw $this->createNotFoundException('Utilisateur non trouvé.');
        }
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

        return $this->redirectToRoute('updateClient', ['id' => $id]);
    }



    #[Route('/user/AddClient', name: 'AddClient')]
    public function AddClient(Request $request): Response
    {
        $client = new Client();
        $form = $this->createForm(UpdateClientType::class, $client);
        
        
        
        $client->setDateNaissance("fokalaomi");
        $form->handleRequest($request);
        if ($form->isSubmitted()   && $form->get('nom')->isValid() && $form->get('prenom')->isValid() && $form->get('email')->isValid() && $form->get('poids')->isValid()  && $form->get('taille')->isValid()) {
            $dateNaissance = $form->get('dateNaissance')->getData();
            if ($dateNaissance instanceof \DateTime) {
                $client->setDateNaissance($dateNaissance->format('Y-m-d'));
            }
            $encryptedPassword = $this->encrypt->encryptAES($form->get('mdp')->getData());
            $client->setMdp($encryptedPassword);
            $client->setImage('C:/xampp/htdocs/images/logo.png');
            $client->setSexe("homme");
            $client->setId(0);
            
            
            
            $this->clientRepository->ajouter($client);
            return $this->redirectToRoute('app_users');
        }
        return $this->render('users/AjouterClient.html.twig', [
            'form' => $form->createView(),
        ]);
    }



    #[Route('/user/AddCoach', name: 'AddCoach')]
    public function AddCoach(Request $request): Response
    {
        $coach = new Coach();
        $form = $this->createForm(CoachType::class, $coach);
        
        
        
        $coach->setDateNaissance("fokalaomi");
        $form->handleRequest($request);
        if ($form->isSubmitted() && $form->isValid()) {
            $dateNaissance = $form->get('dateNaissance')->getData();
            if ($dateNaissance instanceof \DateTime) {
                $coach->setDateNaissance($dateNaissance->format('Y-m-d'));
            }
            $encryptedPassword = $this->encrypt->encryptAES($form->get('mdp')->getData());
            $coach->setMdp($encryptedPassword); 
            $imageFile = $form->get('image')->getData();
            if ($imageFile) {
                $newFilename = uniqid().'.'.$imageFile->guessExtension();
                $imageFile->move('C:/xampp/htdocs/images/', $newFilename);
                $coach->setImage('C:/xampp/htdocs/images/' . $newFilename);
            }
            else {
                $coach->setImage('C:/xampp/htdocs/images/logo.png');
            }
            $coach->setId(0);
            
            
            
            $this->coachRepository->ajouter($coach);
            return $this->redirectToRoute('app_users');
        }
        return $this->render('users/AjouterCoach.html.twig', [
            'form' => $form->createView(),
        ]);
    }



    #[Route('/user/updateCoach/{id}', name: 'updateCoach')]
    public function updateCoach(int $id,Request $request): Response
    {
        $coach = $this->coachRepository->find($id);
        if (!$coach) {
            throw $this->createNotFoundException('Utilisateur non trouvé.');
        }
        $form = $this->createForm(UpdateCoachType::class, $coach);
        $form->handleRequest($request);
        if ($form->isSubmitted() && $form->isValid()) {
            $dateNaissance = $form->get('dateNaissance')->getData();
            if ($dateNaissance instanceof \DateTime) {
                $coach->setDateNaissance($dateNaissance->format('Y-m-d'));
            }
            if ($form->get('mdp')->getData() !== null) {
                $encryptedPassword = $this->encrypt->encryptAES($form->get('mdp')->getData());
                $coach->setMdp($encryptedPassword);
            }

            $this->coachRepository->modifier($coach);
            return $this->redirectToRoute('app_users');
        }
        return $this->render('users/UpdateCoach.html.twig', [
            'form' => $form->createView(),
            'coach' => $coach,
        ]);
    }



    #[Route('/user/updateCoachImage/{id}', name: 'updateCoachImage')]
    public function updateCoachImage(int $id,Request $request): Response
    {
        $coach = $this->coachRepository->find($id);
        if (!$coach) {
            throw $this->createNotFoundException('Utilisateur non trouvé.');
        }
        $imageFile = $request->files->get('image');

        if ($imageFile) {
            $newFilename = uniqid() . '.' . $imageFile->guessExtension();

            try {
                $imageFile->move('C:/xampp/htdocs/images/', $newFilename);
                $coach->setImage('C:/xampp/htdocs/images/' . $newFilename);
                $this->coachRepository->updateImage($coach);

            } catch (\Exception $e) {
                $this->addFlash('error', 'Une erreur est survenue lors du téléchargement de l\'image.');
            }
        }

        return $this->redirectToRoute('updateCoach', ['id' => $id]);
    }



    #[Route('/user/deleteCoach/{id}', name: 'deleteCoach')]
    public function deleteCoach(int $id): Response
    {
        $coach = $this->coachRepository->find($id);
        if ($coach) {
            $this->coachRepository->supprimer($coach);
            return $this->redirectToRoute('app_users');
        } else {
            throw $this->createNotFoundException('Utilisateur non trouvé.');
        }
    }


    #[Route('/users/ListeAdmins', name: 'ListeAdmins')]
    public function ListeAdmins(Request $request): Response
    {
        $admins = $this->AdminRepository->afficher();
        $admin = new Admin();
        if (!$admins) {
            throw $this->createNotFoundException('Aucun admin trouvé.');
        }
        $form = $this->createForm(AdminType::class, $admin);
        $form->handleRequest($request);
        if ($form->isSubmitted() && $form->isValid()) {
            $dateNaissance = $form->get('dateNaissance')->getData();
            if ($dateNaissance instanceof \DateTime) {
                $admin->setDateNaissance($dateNaissance->format('Y-m-d'));
            }
            $imageFile = $form->get('image')->getData();
            if ($imageFile) {
                $newFilename = uniqid().'.'.$imageFile->guessExtension();
                $imageFile->move('C:/xampp/htdocs/images/', $newFilename);
                $admin->setImage('C:/xampp/htdocs/images/' . $newFilename);
            }
            else {
                $admin->setImage('C:/xampp/htdocs/images/logo.png');
            }
            $encryptedPassword = $this->encrypt->encryptAES($form->get('mdp')->getData());
            $admin->setMdp($encryptedPassword);
            $admin->setId(0);
            $this->AdminRepository->ajouter($admin);
            return $this->redirectToRoute('ListeAdmins');
        }
        return $this->render('users/ListeAdmins.html.twig', [
            'admins' => $admins,
            'form' => $form->createView(),
        ]);
    }



    #[Route('/user/deleteAdmin/{id}', name: 'deleteAdmin')]
    public function deleteAdmin(int $id): Response
    {
        $admin = $this->AdminRepository->find($id);
        if ($admin) {
            $this->AdminRepository->supprimer($admin);
            return $this->redirectToRoute('ListeAdmins');
        } else {
            throw $this->createNotFoundException('Utilisateur non trouvé.');
        }
    }


    #[Route('/user/updateAdmin/{id}', name: 'updateAdmin')]
    public function updateAdmin(int $id,Request $request): Response
    {
        $admin = $this->AdminRepository->find($id);
        if (!$admin) {
            throw $this->createNotFoundException('Utilisateur non trouvé.');
        }
        $form = $this->createForm(UpdateAdminType::class, $admin);
        $form->handleRequest($request);
        if ($form->isSubmitted() && $form->isValid()) {
            $dateNaissance = $form->get('dateNaissance')->getData();
            if ($dateNaissance instanceof \DateTime) {
                $admin->setDateNaissance($dateNaissance->format('Y-m-d'));
            }
            if ($form->get('mdp')->getData() !== null) {
                $encryptedPassword = $this->encrypt->encryptAES($form->get('mdp')->getData());
                $admin->setMdp($encryptedPassword);
            }

            $this->AdminRepository->modifier($admin);
            return $this->redirectToRoute('ListeAdmins');
        }
        return $this->render('users/UpdateAdmin.html.twig', [
            'form' => $form->createView(),
            'admin' => $admin,
        ]);
    }


    #[Route('/user/updateAdminImage/{id}', name: 'updateAdminImage')]
    public function updateAdminImage(int $id,Request $request): Response
    {
        $admin = $this->AdminRepository->find($id);
        if (!$admin) {
            throw $this->createNotFoundException('Utilisateur non trouvé.');
        }
        $imageFile = $request->files->get('image');

        if ($imageFile) {
            $newFilename = uniqid() . '.' . $imageFile->guessExtension();

            try {
                $imageFile->move('C:/xampp/htdocs/images/', $newFilename);
                $admin->setImage('C:/xampp/htdocs/images/' . $newFilename);
                $this->AdminRepository->updateImage($admin);

            } catch (\Exception $e) {
                $this->addFlash('error', 'Une erreur est survenue lors du téléchargement de l\'image.');
            }
        }

        return $this->redirectToRoute('updateAdmin', ['id' => $id]);
    }


    #[Route('/user/setSupAdmin/{id}', name: 'setSupAdmin')]
    public function setSupAdmin(int $id,Request $request): Response
    {
        $admin = $this->AdminRepository->find($id);
        if (!$admin) {
            throw $this->createNotFoundException('Utilisateur non trouvé.');
        }
        $admin->setRole("supadmin");
        $this->AdminRepository->SetRole($admin);

        return $this->redirectToRoute('ListeAdmins');
    }

    #[Route('/user/setAdmin/{id}', name: 'setAdmin')]
    public function setAdmin(int $id,Request $request): Response
    {
        $admin = $this->AdminRepository->find($id);
        if (!$admin) {
            throw $this->createNotFoundException('Utilisateur non trouvé.');
        }
        $admin->setRole("admin");
        $this->AdminRepository->SetRole($admin);

        return $this->redirectToRoute('ListeAdmins');
    }




}
