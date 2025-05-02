<?php

namespace App\Controller;

use App\Entity\RatingActivity;
use App\Repository\RatingActivityRepository;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Attribute\Route;

final class RatingActivityController extends AbstractController
{
    #[Route('/ratings', name: 'admin_ratings_index')]
    public function index(RatingActivityRepository $ratingActivityRepo): Response
    {
        $ratings = $ratingActivityRepo->createQueryBuilder('ra')
            ->leftJoin('ra.activity', 'a')
            ->leftJoin('ra.salle', 's')
            ->leftJoin('ra.client', 'c')
            ->addSelect('a', 's', 'c')
            ->orderBy('ra.ratingStars', 'DESC')
            ->getQuery()
            ->getResult();

        return $this->render('rating_activity/index.html.twig', [
            'ratings' => $ratings,
        ]);
    }

    #[Route('/ratings/{idActivity}/{idSalle}/{idClient}', 
        name: 'admin_ratings_show',
        requirements: [
            'idActivity' => '\d+',
            'idSalle' => '\d+', 
            'idClient' => '\d+'
        ]
    )]
    public function show(
        int $idActivity,
        int $idSalle,
        int $idClient,
        RatingActivityRepository $ratingActivityRepo
    ): Response {
        $rating = $ratingActivityRepo->findOneBy([
            'activity' => $idActivity,
            'salle' => $idSalle,
            'client' => $idClient
        ]);

        if (!$rating) {
            throw $this->createNotFoundException('Évaluation non trouvée');
        }

        return $this->render('rating_activity/show.html.twig', [
            'rating' => $rating,
        ]);
    }
}