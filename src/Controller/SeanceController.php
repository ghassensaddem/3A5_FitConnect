<?php

namespace App\Controller;

use App\Entity\Seance;
use App\Form\SeanceType;
use App\Repository\SeanceRepository;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Attribute\Route;
use Symfony\Component\HttpFoundation\JsonResponse;

#[Route('/seance')]
final class SeanceController extends AbstractController
{
    #[Route(name: 'app_seance_index', methods: ['GET'])]
    public function index(SeanceRepository $seanceRepository): Response
    {
        $seances = $seanceRepository->findAll();
    
        $events = [];
    
        foreach ($seances as $seance) {
            $events[] = [
                'id' => $seance->getId(),
                'title' => 'Séance ' . $seance->getId(),
                'start' => $seance->getDate()->format('Y-m-d') . 'T' . $seance->getHoraire()->format('H:i:s'),
                'extendedProps' => [
                    'editUrl' => $this->generateUrl('app_seance_edit', ['id' => $seance->getId()]),
                    'deleteUrl' => $this->generateUrl('app_seance_delete', ['id' => $seance->getId()]),
                ],
            ];
            
        }
        
        return $this->render('seance/index.html.twig', [
            'seances' => $seances,
            'events' => $events, // Passer les événements avec les URLs
        ]);
    }
    
    // Autres méthodes pour new, edit, show, delete...

    
    
    

    #[Route('/new', name: 'app_seance_new', methods: ['GET', 'POST'])]
    public function new(Request $request, EntityManagerInterface $entityManager): Response
    {
        $seance = new Seance();
       
        $form = $this->createForm(SeanceType::class, $seance);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $entityManager->persist($seance);
            $entityManager->flush();
            $historique = new \App\Entity\Historique();
            $historique->setAction('ajout');
            $historique->setEntite('seance');
            $historique->setDate(new \DateTime());
            $historique->setDetails('Ajout de la seance : ' . $seance->getId());
    
            $entityManager->persist($historique);
            $entityManager->flush();
            return $this->redirectToRoute('app_seance_index', [], Response::HTTP_SEE_OTHER);
        }

        return $this->render('seance/new.html.twig', [
            'seance' => $seance,
            'form' => $form,
        ]);
    }

    #[Route('/{id}', name: 'app_seance_show', methods: ['GET'])]
    public function show(Seance $seance): Response
    {
        return $this->render('seance/show.html.twig', [
            'seance' => $seance,
        ]);
    }

    #[Route('/{id}/edit', name: 'app_seance_edit', methods: ['GET', 'POST'])]
    public function edit(Request $request, Seance $seance, EntityManagerInterface $entityManager): Response
    {
        $form = $this->createForm(SeanceType::class, $seance);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $entityManager->flush();
            $entityManager->persist($seance);
            $entityManager->flush();
            $historique = new \App\Entity\Historique();
            $historique->setAction('modification');
            $historique->setEntite('seance');
            $historique->setDate(new \DateTime());
            $historique->setDetails('Modification de la seance : ' . $seance->getId());
    
            $entityManager->persist($historique);
            $entityManager->flush();
            return $this->redirectToRoute('app_seance_index', [], Response::HTTP_SEE_OTHER);
        }

        return $this->render('seance/edit.html.twig', [
            'seance' => $seance,
            'form' => $form,
        ]);
    }

    #[Route('/{id}', name: 'app_seance_delete', methods: ['POST'])]
    public function delete(Request $request, Seance $seance, EntityManagerInterface $entityManager): Response
    {
        if (true) {
        
         
    
            // Historique (ok)
            $historique = new \App\Entity\Historique();
            $historique->setAction('suppression');
            $historique->setEntite('seance');
            $historique->setDate(new \DateTime());
            $historique->setDetails('Suppression de la séance : ' . $seance->getId());
    
            $entityManager->persist($historique);
            
            $entityManager->remove($seance);
            $entityManager->flush();
        }
    
        // Voici la seule chose à faire pour un fetch:
        return new JsonResponse(['status' => 'ok']);
    }
    

    

}
