<?php

namespace App\Controller;

use App\Entity\RatingActivity;
use App\Entity\Sallesportif;
use App\Repository\RatingActivityRepository;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Attribute\Route;
use App\Service\TwilioCaller;
use Symfony\Component\HttpFoundation\JsonResponse;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\DependencyInjection\ParameterBag\ParameterBagInterface;
use Psr\Log\LoggerInterface;
use App\Entity\Client;
use Doctrine\ORM\EntityManagerInterface;
use App\Repository\PlanningActivityRepository;






final class RatingActivityController extends AbstractController
{
    #[Route('/ratings', name: 'admin_ratings_index')]
public function index(
    RatingActivityRepository $ratingActivityRepo,
    ParameterBagInterface $params,
    PlanningActivityRepository $planningRepo
): Response {
    // Récupération des évaluations avec jointures
    $ratings = $ratingActivityRepo->createQueryBuilder('ra')
        ->leftJoin('ra.activity', 'a')
        ->leftJoin('ra.salle', 's')
        ->leftJoin('ra.client', 'c')
        ->addSelect('a', 's', 'c')
        ->orderBy('ra.ratingStars', 'DESC')
        ->getQuery()
        ->getResult();

    // Logique de statistiques des commentaires
    $commentairesParActivite = [];
    $salles = [];
    
    foreach ($ratings as $rating) {
        $activity = $rating->getActivity();
        $salle = $rating->getSalle();
        
        if ($activity && $salle) {
            // Statistiques par activité
            $activityId = $activity->getIdActivity();
            $commentairesParActivite[$activityId] = [
                'count' => ($commentairesParActivite[$activityId]['count'] ?? 0) + 1,
                'nom' => $activity->getNomActivity()
            ];
            
            // Liste des salles pour référence
            $salles[$salle->getIdSalle()] = $salle;
        }
    }
    
    // Calcul du total et préparation des données du graphique
    $totalCommentaires = array_sum(array_column($commentairesParActivite, 'count'));
    $chartData = [];
    
    foreach ($commentairesParActivite as $activityId => $data) {
        $pourcentage = $totalCommentaires > 0 
            ? ($data['count'] * 100) / $totalCommentaires 
            : 0;
            
        $chartData[] = [
            'label' => $data['nom'],
            'value' => round($pourcentage, 2),
            'quantite' => $data['count'],
            'color' => $this->generateRandomColor()
        ];
    }

    return $this->render('rating_activity/index.html.twig', [
        'ratings' => $ratings,
        'chartData' => $chartData,
        'totalCommentaires' => $totalCommentaires,
        'salles' => $salles, // Optionnel: si vous voulez utiliser les infos des salles
        'default_phone' => $params->has('DEFAULT_CLIENT_PHONE') 
            ? $params->get('DEFAULT_CLIENT_PHONE') 
            : '48048587'
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

    #[Route('/call-client', name: 'call_client', methods: ['POST'])]
public function callClient(
    Request $request, 
    TwilioCaller $twilio,
    EntityManagerInterface $em,
    LoggerInterface $logger
): JsonResponse {
    // 1. Récupération du client statique


     /* 
        ClientRepository $clientRepository,
        if (!$user) {
            throw $this->createAccessDeniedException('Vous devez être connecté pour accéder à cette page.');
        }
        if (!$user instanceof Client) {
            throw new \LogicException('Utilisateur invalide.');
        }
             $user = $security->getUser();
            */
    $client = $em->getRepository(Client::class)->find(2);

    
    
    if (!$client) {
        $logger->error('Client statique non trouvé', ['client_id' => 2]);
        return $this->json([
            'status' => 'error',
            'message' => 'Client configuration manquante'
        ], 500);
    }

    // 2. Vérification du numéro de téléphone
    $phone = $request->request->get('phone');
    if (empty($phone)) {
        $logger->warning('Numéro de téléphone manquant', ['client' => $client->getId()]);
        return $this->json([
            'status' => 'error',
            'message' => 'Numéro de téléphone requis'
        ], 400);
    }

    // 3. Exécution de l'appel
    try {
        $callSid = $twilio->sendCommunityReminder('+21648048587'); // Hardcodé pour client #2        
        $logger->info('Appel Twilio réussi', [
            'client' => $client->getId(),
            'callSid' => $callSid,
            'phone' => $phone
        ]);
        
        return $this->json([
            'status' => 'success',
            'message' => 'Rappel envoyé au client #'.$client->getId(),
            'client' => $client->getNom().' '.$client->getPrenom(), // Utilisez les getters existants
            'callSid' => $callSid
        ]);
        
    } catch (\Exception $e) {
        $logger->error('Échec de l\'appel Twilio', [
            'client' => $client->getId(),
            'error' => $e->getMessage(),
            'trace' => $e->getTraceAsString()
        ]);
        
        return $this->json([
            'status' => 'error',
            'message' => 'Échec technique pour le client #'.$client->getId(),
            'error' => $_SERVER['APP_ENV'] === 'dev' ? $e->getMessage() : null
        ], 500);
    }
}


// Fonction pour générer des couleurs aléatoires (reprise de la première fonction)
private function generateRandomColor(): string
{
    return sprintf('#%06X', mt_rand(0, 0xFFFFFF));
}



}