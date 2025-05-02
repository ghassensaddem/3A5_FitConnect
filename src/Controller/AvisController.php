<?php

namespace App\Controller;
use App\Entity\Seance; // Assure-toi que cette ligne est présente

use App\Entity\Avis;
use App\Form\AvisType;
use App\Repository\AvisRepository;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Attribute\Route;
use Symfony\Component\Security\Core\Security; // n'oublie pas d'ajouter ce use si nécessaire




#[Route('/avis')]
final class AvisController extends AbstractController
{
    #[Route(name: 'app_avis_index', methods: ['GET'])]
    public function index(AvisRepository $avisRepository): Response
{
    $avis = $avisRepository->findAll();
    $somme = 0;
    $nombre = count($avis);

    foreach ($avis as $avi) {
        $somme += $avi->getNote();
    }

    $moyenne = $nombre > 0 ? $somme / $nombre : 0;

    return $this->render('avis/index.html.twig', [
        'avis' => $avis,
        'moyenne' => $moyenne,
    ]);
}

    #[Route('/back', name: 'app_avis_back_index', methods: ['GET'])]
    public function backIndex(AvisRepository $avisRepository): Response
    {
        return $this->render('avis/back.html.twig', [
            'avis' => $avisRepository->findAll(),
        ]);
    }
   
    #[Route('/new', name: 'app_avis_new', methods: ['GET', 'POST'])]
    public function new(Request $request, EntityManagerInterface $entityManager): Response
    {
        // Récupère l'id de la séance depuis l'URL
        $seanceId = $request->query->get('id');
        
        // Vérifie si l'id de la séance est fourni et valide
        $seance = $entityManager->getRepository(Seance::class)->find($seanceId);

        if (!$seance) {
            throw $this->createNotFoundException('Séance non trouvée.');
        }

        // Créer une nouvelle instance de l'avis
        $avi = new Avis();
        
        // Associer la séance à l'avis
        $avi->setSeanceid($seance);

        // Crée le formulaire pour l'avis
        $form = $this->createForm(AvisType::class, $avi);

        $form->handleRequest($request);

        // Si le formulaire est soumis et valide
        if ($form->isSubmitted() && $form->isValid()) {
       
            // Persiste l'avis
            $entityManager->persist($avi);
            $entityManager->flush();

            // Redirige vers la page d'index des avis
            return $this->redirectToRoute('app_avis_index');
        }

        // Retourne le formulaire de création de l'avis
        return $this->render('avis/new.html.twig', [
            'form' => $form->createView(),
        ]);
    }    

    #[Route('/{id}/edit', name: 'app_avis_edit', methods: ['GET', 'POST'])]
    public function edit(Request $request, Avis $avi, EntityManagerInterface $entityManager): Response
    {
        $form = $this->createForm(AvisType::class, $avi);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $entityManager->flush();

            return $this->redirectToRoute('app_avis_index', [], Response::HTTP_SEE_OTHER);
        }

        return $this->render('avis/edit.html.twig', [
            'avi' => $avi,
            'form' => $form,
        ]);
    }

    #[Route('/{id}', name: 'app_avis_delete', methods: ['POST'])]
    public function delete(Request $request, Avis $avi, EntityManagerInterface $entityManager): Response
    {
        // Vérification du token CSRF
        if ($this->isCsrfTokenValid('delete'.$avi->getId(), $request->request->get('_token'))) {
            // Retirer l'avis de la séance associée
            if ($avi->getSeanceid()) {
                $avi->getSeanceid()->removeAvi($avi);  // Dissocier l'avis de la séance
            }
    
            // Supprimer l'avis
            $entityManager->remove($avi);
            $entityManager->flush();
        }
        $returnTo = $request->request->get('returnTo');

    if ($returnTo) {
        return $this->redirect($returnTo);
    }

    
        // Redirection vers la liste des avis
        return $this->redirectToRoute('app_avis_index', [], Response::HTTP_SEE_OTHER);
    }
    
    
}    