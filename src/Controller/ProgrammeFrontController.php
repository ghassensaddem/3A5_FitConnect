<?php

// src/Controller/ProgrammeFrontController.php
// src/Controller/ProgrammeFrontController.php
namespace App\Controller;

use App\Repository\ProgrammeRepository;
use App\Repository\SeanceRepository;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;

class ProgrammeFrontController extends AbstractController
{
    #[Route('/programme/front', name: 'app_programme_front')]
    public function index(ProgrammeRepository $programmeRepository, SeanceRepository $seanceRepository): Response
    {
        // Récupérer tous les programmes et séances
        $programmes = $programmeRepository->findAll();
        $seances = $seanceRepository->findAll();

        // Vérification des données (optionnel, mais utile pour éviter les erreurs)
        if (!$programmes) {
            throw $this->createNotFoundException('Aucun programme trouvé.');
        }
        if (!$seances) {
            throw $this->createNotFoundException('Aucune séance trouvée.');
        }

        // Retourner la vue avec les données
        return $this->render('programme_front/index.html.twig', [
            'programmes' => $programmes,
            'seances' => $seances,
        ]);
    }

    #[Route('/programme/{id}', name: 'app_programme_show')]
    public function show(int $id, ProgrammeRepository $programmeRepository): Response
    {
        // Récupérer le programme spécifique par son id
        $programme = $programmeRepository->find($id);

        // Vérification si le programme existe
        if (!$programme) {
            throw $this->createNotFoundException('Programme non trouvé.');
        }

        // Retourner la vue pour afficher un programme spécifique
        return $this->render('programme/show.html.twig', [
            'programme' => $programme,
        ]);
    }
}

