<?php

namespace App\Controller;

use App\Entity\Programme;
use App\Form\ProgrammeType;
use App\Repository\ProgrammeRepository;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Attribute\Route;
use App\Entity\Historique;

#[Route('/programme')]
final class ProgrammeController extends AbstractController
{
    #[Route('/', name: 'app_programme_index', methods: ['GET'])]
    public function index(Request $request, ProgrammeRepository $programmeRepository): Response
    {
        $ordre = $request->query->get('ordre'); // tri par défaut
    
        // Sécurité : on ne garde que 'asc' ou 'desc'
        if (in_array(strtolower($ordre), ['asc', 'desc'])) {
            $programmes = $programmeRepository->findBy([], ['prix' => strtoupper($ordre)]);
        } else {
            $programmes = $programmeRepository->findAll(); // pas de tri
        }
    
        // Statistiques
        $prixSuperieur100 = 0;
        $prixInferieurOuEgal100 = 0;
    
        foreach ($programmes as $programme) {
            if ($programme->getPrix() > 100) {
                $prixSuperieur100++;
            } else {
                $prixInferieurOuEgal100++;
            }
        }
    
        return $this->render('programme/index.html.twig', [
            'programmes' => $programmes,
            'superieur' => $prixSuperieur100,
            'inferieur' => $prixInferieurOuEgal100,
            'ordre' => $ordre, // utile pour l'affichage ou pour savoir dans quel ordre on est
        ]);
    }
    
    #[Route('/new', name: 'app_programme_new', methods: ['GET', 'POST'])]
    public function new(Request $request, EntityManagerInterface $entityManager): Response
    {
        $programme = new Programme();
        $form = $this->createForm(ProgrammeType::class, $programme);
        $form->handleRequest($request);
    
        if ($form->isSubmitted() && $form->isValid()) {
            $entityManager->persist($programme);
            $entityManager->flush();
    
            // 🔥 Enregistrement dans l’historique après l’ajout du programme
            $historique = new \App\Entity\Historique();
            $historique->setAction('ajout');
            $historique->setEntite('Programme');
            $historique->setDate(new \DateTime());
            $historique->setDetails('Ajout du programme : ' . $programme->getId());
    
            $entityManager->persist($historique);
            $entityManager->flush();
    
            return $this->redirectToRoute('app_programme_index', [], Response::HTTP_SEE_OTHER);

        }
    
        return $this->render('programme/new.html.twig', [
            'programme' => $programme,
            'form' => $form,
        ]);
    }
    

    #[Route('/{id}', name: 'app_programme_show', methods: ['GET'])]
    public function show(Programme $programme): Response
    {
        return $this->render('programme/show.html.twig', [
            'programme' => $programme,
        ]);
    }

    #[Route('/{id}/edit', name: 'app_programme_edit', methods: ['GET', 'POST'])]
    public function edit(Request $request, Programme $programme, EntityManagerInterface $entityManager): Response
    {
        $form = $this->createForm(ProgrammeType::class, $programme);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $entityManager->flush();
            $historique = new \App\Entity\Historique();
            $historique->setAction('modifier');
            $historique->setEntite('Programme');
            $historique->setDate(new \DateTime());
            $historique->setDetails('modification du programme : ' . $programme->getId());
    
            $entityManager->persist($historique);
            $entityManager->flush();
            return $this->redirectToRoute('app_programme_index', [], Response::HTTP_SEE_OTHER);
        }

        return $this->render('programme/edit.html.twig', [
            'programme' => $programme,
            'form' => $form,
        ]);
    }

    #[Route('/{id}', name: 'app_programme_delete', methods: ['POST'])]
    public function delete(Request $request, Programme $programme, EntityManagerInterface $entityManager): Response
    {
        if ($this->isCsrfTokenValid('delete'.$programme->getId(), $request->getPayload()->getString('_token'))) {
           
         
            $historique = new \App\Entity\Historique();
            $historique->setAction('suppression');
            $historique->setEntite('Programme');
            $historique->setDate(new \DateTime());
            $historique->setDetails('Suppression du programme : ' . $programme->getId());
    
            $entityManager->persist($historique);
            $entityManager->remove($programme);
            $entityManager->flush();
    
           
        }

        return $this->redirectToRoute('app_programme_index', [], Response::HTTP_SEE_OTHER);
    }
   
    
    

}
