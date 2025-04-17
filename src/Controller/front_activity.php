<?php
namespace App\Controller;

use App\Entity\CategorieActivity;
use App\Entity\Client;
use App\Entity\Sallesportif;
use App\Entity\Activity;
use App\Entity\RatingActivity;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\HttpFoundation\Request; // <-- Import manquant
use Symfony\Component\Routing\Annotation\Route;
use Doctrine\Persistence\ManagerRegistry;
use App\Repository\ActivityRepository;
use App\Form\RatingType;
use App\Repository\ClientRepository;
use App\Repository\RatingActivityRepository;
use Symfony\Component\HttpFoundation\JsonResponse;



class front_activity extends AbstractController
{
    private ManagerRegistry $managerRegistry;
    private ActivityRepository $activityRepository;


    public function __construct(
        ManagerRegistry $managerRegistry,
        ActivityRepository $activityRepository
    ) {
        $this->managerRegistry = $managerRegistry;
        $this->activityRepository = $activityRepository;
    }

    #[Route('/test-front', name: 'test_front')]
    public function index(): Response
    {
        $categories = $this->managerRegistry->getRepository(CategorieActivity::class)->findAll();
        $activities = $this->managerRegistry->getRepository(Activity::class)->findAll();

        return $this->render('activity_front/activityFront.html.twig', [
            'categories' => $categories,
            'activities' => $activities,
            'selectedCategory' => null
        ]);
    }

    #[Route('/category/{id}', name: 'category_activities')]
public function showCategoryActivities(int $id): Response
{
    $category = $this->managerRegistry->getRepository(CategorieActivity::class)->find($id);
    
    if (!$category) {
        throw $this->createNotFoundException('Catégorie non trouvée');
    }

    $categories = $this->managerRegistry->getRepository(CategorieActivity::class)->findAll();

    return $this->render('activity_front/activityFront.html.twig', [
        'categories' => $categories,
        'activities' => $category->getActivities(),
        'selectedCategory' => $id 
    ]);
}
#[Route('/activities/{id}/details', name: 'activity_details')]
public function showActivityDetails(int $id): Response
{
    $activity = $this->activityRepository->find($id);
    if (!$activity) {
        throw $this->createNotFoundException('Activité non trouvée');
    }

    return $this->render('activity_front/activity_details.html.twig', [
        'activity' => $activity
    ]);
}

#[Route('/activities/{id}', name: 'activity_salle')]
public function showActivitySalles(int $id): Response
{
    $activity = $this->activityRepository->findWithSalles($id);
    if (!$activity) {
        throw $this->createNotFoundException('Activité non trouvée');
    }

    return $this->render('activity_front/activity_salle.html.twig', [
        'activity' => $activity,
        'salles' => $activity->getSallesAssociees()
    ]);
}

#[Route('/activities/{activityId}/salle/{salleId}/rate', name: 'rate_salle')]
public function rateSalle(
    int $activityId, 
    int $salleId, 
    Request $request,
    ClientRepository $clientRepository,
    RatingActivityRepository $ratingRepository
): Response {
    $activity = $this->activityRepository->find($activityId);
    $salle = $this->managerRegistry->getRepository(Sallesportif::class)->find($salleId);
    $user = $clientRepository->find(2); // Remplacez par $this->getUser() en production

    if (!$activity || !$salle) {
        throw $this->createNotFoundException('Activité ou salle non trouvée');
    }

    // Cherche un rating existant ou crée un nouveau
    $rating = $ratingRepository->findUserRating($user, $activity, $salle) ?? new RatingActivity();
    
    $form = $this->createForm(RatingType::class, $rating);
    $form->handleRequest($request);

    if ($form->isSubmitted() && $form->isValid()) {
        $rating->setActivity($activity)
               ->setSalle($salle)
               ->setClient($user);
        
        $em = $this->managerRegistry->getManager();
        $em->persist($rating);
        $em->flush();

        $this->addFlash('success', 'Merci pour votre avis!');
        return $this->redirectToRoute('rate_salle', [
            'activityId' => $activityId,
            'salleId' => $salleId
        ]);
    }

    // Récupère tous les avis pour cette salle et activité
    $ratings = $ratingRepository->findBy([
        'activity' => $activity,
        'salle' => $salle
    ]);

    return $this->render('activity_front/rate_salle.html.twig', [
        'activity' => $activity,
        'salle' => $salle,
        'ratings' => $ratings,
        'form' => $form->createView(),
        'userRating' => $ratingRepository->findUserRating($user, $activity, $salle),
        'currentUserId' => 2 // ID fixe
    ]);
}
#[Route('/activities/{activityId}/salle/{salleId}/comments', name: 'view_comments')]
public function viewComments(int $activityId, int $salleId, RatingActivityRepository $ratingRepo): Response
{
    $activity = $this->activityRepository->find($activityId);
    $salle = $this->managerRegistry->getRepository(Sallesportif::class)->find($salleId);

    if (!$activity || !$salle) {
        throw $this->createNotFoundException('Activité ou salle non trouvée');
    }

    $ratings = $ratingRepo->findBy([
        'activity' => $activity,
        'salle' => $salle
    ]);

    return $this->render('activity_front/view_comments.html.twig', [
        'activity' => $activity,
        'salle' => $salle,
        'ratings' => $ratings
    ]);
}

#[Route('/activities/{activityId}/salle/{salleId}/client/{clientId}', name: 'delete_rating', methods: ['DELETE'])]
public function deleteRating(
    int $activityId, 
    int $salleId, 
    int $clientId,
    RatingActivityRepository $ratingRepository
): Response {
   

    $rating = $ratingRepository->findOneBy([
        'activity' => $activityId,
        'salle' => $salleId,
        'client' => $clientId
    ]);

    if (!$rating) {
        throw $this->createNotFoundException('Commentaire non trouvé');
    }

    $entityManager = $this->managerRegistry->getManager();
    $entityManager->remove($rating);
    $entityManager->flush();

    return new JsonResponse(['status' => 'success'], Response::HTTP_OK);
}

#[Route('/activities/{activityId}/salle/{salleId}/update', name: 'update_rating', methods: ['POST'])]
public function updateRating(
    int $activityId,
    int $salleId,
    Request $request,
    ClientRepository $clientRepository,
    RatingActivityRepository $ratingRepository
): JsonResponse {
    $user = $clientRepository->find(2); // ID fixe à 1
    $activity = $this->activityRepository->find($activityId);
    $salle = $this->managerRegistry->getRepository(Sallesportif::class)->find($salleId);

    $data = json_decode($request->getContent(), true);

    $rating = $ratingRepository->findOneBy([
        'activity' => $activity,
        'salle' => $salle,
        'client' => $user
    ]);

    if (!$rating) {
        return new JsonResponse(['error' => 'Rating not found'], Response::HTTP_NOT_FOUND);
    }

    $rating->setRatingStars($data['ratingStars']);
    $rating->setReview($data['review']);

    $this->managerRegistry->getManager()->flush();

    return new JsonResponse([
        'status' => 'success',
        'newRating' => $rating->getRatingStars(),
        'newReview' => $rating->getReview()
    ]);
}
}