<?php

namespace App\Controller;

use App\Entity\PlanningActivity;
use App\Form\PlanningActivityType;
use App\Repository\PlanningActivityRepository;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;
use Psr\Log\LoggerInterface;


#[Route('/planning-activity')]
class PlanningActivityController extends AbstractController
{
    #[Route('/', name: 'app_planning_activity_index', methods: ['GET'])]
    public function index(PlanningActivityRepository $planningActivityRepository): Response
    {
    return $this->render('planning_activity/index.html.twig', [
        'planning_activities' => $planningActivityRepository->findAllWithDetails(),
    ]);
    }

    #[Route('/new', name: 'app_planning_activity_new', methods: ['GET', 'POST'])]
    public function new(Request $request, EntityManagerInterface $entityManager): Response
    {
        $planningActivity = new PlanningActivity();
        $form = $this->createForm(PlanningActivityType::class, $planningActivity);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            // Validation supplémentaire
            if ($planningActivity->getNombreInscription() > $planningActivity->getCapacityMax()) {
                $this->addFlash('error', 'Le nombre d\'inscriptions ne peut pas dépasser la capacité maximale');
                return $this->redirectToRoute('app_planning_activity_new');
            }
            
            $entityManager->persist($planningActivity);
            $entityManager->flush();

            return $this->redirectToRoute('app_planning_activity_index', [], Response::HTTP_SEE_OTHER);
        }

        return $this->render('planning_activity/new.html.twig', [
            'planning_activity' => $planningActivity,
            'form' => $form,
        ]);
    }

    #[Route('/{activityId}/{salleId}', name: 'app_planning_activity_show', methods: ['GET'])]
public function show(int $activityId, int $salleId, PlanningActivityRepository $repo): Response
{
    $planningActivity = $repo->findOneBy([
        'activity' => $activityId,
        'salle' => $salleId
    ]);
    
    if (!$planningActivity) {
        throw $this->createNotFoundException('Planning non trouvé');
    }

    return $this->render('planning_activity/show.html.twig', [
        'planning_activity' => $planningActivity,
    ]);
}

#[Route('/{activityId}/{salleId}/edit', name: 'app_planning_activity_edit', methods: ['GET', 'POST'])]
public function edit(
    Request $request, 
    int $activityId, 
    int $salleId,
    PlanningActivityRepository $repo,
    EntityManagerInterface $entityManager
): Response {
    $planningActivity = $repo->findOneBy([
        'activity' => $activityId,
        'salle' => $salleId
    ]);
        $form = $this->createForm(PlanningActivityType::class, $planningActivity);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            // Validation supplémentaire
            if ($planningActivity->getNombreInscription() > $planningActivity->getCapacityMax()) {
                $this->addFlash('error', 'Le nombre d\'inscriptions ne peut pas dépasser la capacité maximale');
                return $this->render('planning_activity/edit.html.twig', [
                    'planning_activity' => $planningActivity,
                    'form' => $form,
                ]);
            }
            
            $entityManager->flush();

            return $this->redirectToRoute('app_planning_activity_index', [], Response::HTTP_SEE_OTHER);
        }

        return $this->render('planning_activity/edit.html.twig', [
            'planning_activity' => $planningActivity,
            'form' => $form,
        ]);
    }

    #[Route('/{activityId}/{salleId}', name: 'app_planning_activity_delete', methods: ['POST'])]
public function delete(
    Request $request,
    int $activityId,
    int $salleId,
    PlanningActivityRepository $repo,
    EntityManagerInterface $entityManager,
    LoggerInterface $logger
): Response {
    $planningActivity = $repo->findOneBy([
        'activity' => $activityId,
        'salle' => $salleId
    ]);
    
    if (!$planningActivity) {
        $this->addFlash('error', 'Planning non trouvé');
        return $this->redirectToRoute('app_planning_activity_index');
    }

    $csrfToken = $request->request->get('_token');
    if (!$this->isCsrfTokenValid('delete_'.$activityId.'_'.$salleId, $csrfToken)) {
        $this->addFlash('error', 'Token CSRF invalide');
        return $this->redirectToRoute('app_planning_activity_index');
    }

    try {
        $entityManager->remove($planningActivity);
        $entityManager->flush();
        $this->addFlash('success', 'Planning supprimé avec succès');
    } catch (\Exception $e) {
        $logger->error('Erreur lors de la suppression', ['error' => $e->getMessage()]);
        $this->addFlash('error', 'Erreur lors de la suppression');
    }
    
    return $this->redirectToRoute('app_planning_activity_index');
}
}