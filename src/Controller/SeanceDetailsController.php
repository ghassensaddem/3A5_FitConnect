<?php

namespace App\Controller;

use App\Repository\SeanceRepository;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;

class SeanceDetailsController extends AbstractController
{
    // Cette méthode permet d'afficher les détails d'une séance spécifique
    #[Route('/seance/{id}/details', name: 'app_seance_details')]
    public function seanceDetails(int $id, SeanceRepository $seanceRepository): Response
    {
        // Récupérer la séance depuis la base de données
        $seance = $seanceRepository->find($id);

        // Si la séance n'est pas trouvée, une erreur 404 est renvoyée
        if (!$seance) {
            throw $this->createNotFoundException('Séance non trouvée.');
        }

        // Retourner la vue avec les informations de la séance
        return $this->render('seance/details.html.twig', [
            'seanceId' => $seance->getId(),
            'date' => $seance->getDate()->format('Y-m-d'),
            'horaire' => $seance->getHoraire()->format('H:i'),
            'programme' => $seance->getProgrammeId()->getDescription(),
            'activite' => $seance->getActiviteId()->getNomActivity(),
        ]);
    }
}
