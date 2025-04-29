<?php

// src/Controller/HistoriqueController.php
namespace App\Controller;

use App\Entity\Historique;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;

#[Route('/historique')]
class HistoriqueController extends AbstractController
{
    #[Route('/', name: 'app_historique_index', methods: ['GET'])]
    public function index(EntityManagerInterface $em): Response
    {
        $historiques = $em->getRepository(Historique::class)->findBy([], ['date' => 'DESC']);

        return $this->render('historique/index.html.twig', [
            'historiques' => $historiques,
        ]);
    }


#[Route('/{id}/delete', name: 'app_historique_delete', methods: ['POST'])]
public function delete(EntityManagerInterface $em, int $id): Response
{
    // Trouver l'enregistrement à supprimer
    $historique = $em->getRepository(Historique::class)->find($id);

    if (!$historique) {
        // Si l'historique n'existe pas, rediriger avec un message d'erreur
        $this->addFlash('error', 'L\'historique n\'existe pas.');
        return $this->redirectToRoute('app_historique_index');
    }

    // Supprimer l'historique
    $em->remove($historique);
    $em->flush();

    // Rediriger après suppression
    $this->addFlash('success', 'L\'historique a été supprimé.');
    return $this->redirectToRoute('app_historique_index');
}
}