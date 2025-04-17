<?php 

namespace App\Controller;

use App\Entity\Activiteevent;
use App\Form\ActiviteeventType;
use App\Repository\ActiviteeventRepository;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;
use Psr\Log\LoggerInterface;
use Symfony\Component\HttpFoundation\JsonResponse;

#[Route('/activiteevent')]
class ActiviteeventController extends AbstractController
{
    private $logger;

    public function __construct(LoggerInterface $logger)
    {
        $this->logger = $logger;
    }

    #[Route('/', name: 'app_activiteevent_index', methods: ['GET'])]
    public function index(ActiviteeventRepository $activiteeventRepository): Response
    {
        try {
            return $this->render('activiteevent/index.html.twig', [
                'activiteevents' => $activiteeventRepository->findAll(),
            ]);
        } catch (\Exception $e) {
            $this->logger->error('Erreur lors de la récupération des activités: '.$e->getMessage());
            throw $this->createNotFoundException('Impossible de charger la liste des activités');
        }
    }

    #[Route('/new', name: 'app_activiteevent_new', methods: ['GET', 'POST'])]
    public function new(Request $request, EntityManagerInterface $entityManager): Response
    {
        $activiteevent = new Activiteevent();
        $form = $this->createForm(ActiviteeventType::class, $activiteevent);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            try {
                $entityManager->persist($activiteevent);
                $entityManager->flush();
                
                $this->addFlash('success', 'Activité créée avec succès');
                return $this->redirectToRoute('app_activiteevent_index', [], Response::HTTP_SEE_OTHER);
            } catch (\Exception $e) {
                $this->logger->error('Erreur création activité: '.$e->getMessage());
                $this->addFlash('error', 'Erreur lors de la création');
            }
        }

        return $this->render('activiteevent/new.html.twig', [
            'activiteevent' => $activiteevent,
            'form' => $form->createView(),
        ]);
    }

    #[Route('/{id}', name: 'app_activiteevent_show', methods: ['GET'])]
    public function show(?Activiteevent $activiteevent): Response
    {
        if (!$activiteevent) {
            $this->addFlash('error', 'L\'activité demandée n\'existe pas');
            return $this->redirectToRoute('app_activiteevent_index');
        }
    
        return $this->render('activiteevent/show.html.twig', [
            'activiteevent' => $activiteevent,
        ]);
    }
    #[Route('/{id}/edit', name: 'app_activiteevent_edit', methods: ['GET', 'POST'])]
    public function edit(Request $request, ?Activiteevent $activiteevent, EntityManagerInterface $entityManager): Response
    {
        if (!$activiteevent) {
            throw $this->createNotFoundException('Activité à modifier non trouvée');
        }

        $form = $this->createForm(ActiviteeventType::class, $activiteevent);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            try {
                $entityManager->flush();
                $this->addFlash('success', 'Activité mise à jour avec succès');
                return $this->redirectToRoute('app_activiteevent_index', [], Response::HTTP_SEE_OTHER);
            } catch (\Exception $e) {
                $this->logger->error('Erreur modification activité ID '.$activiteevent->getId().': '.$e->getMessage());
                $this->addFlash('error', 'Erreur lors de la mise à jour');
            }
        }

        return $this->render('activiteevent/edit.html.twig', [
            'activiteevent' => $activiteevent,
            'form' => $form->createView(),
        ]);
    }

    #[Route('/{id}', name: 'app_activiteevent_delete', methods: ['POST'])]
    public function delete(
        Request $request, 
        ?Activiteevent $activiteevent, 
        EntityManagerInterface $entityManager
    ): Response {
        if (!$activiteevent) {
            throw $this->createNotFoundException('Activité à supprimer non trouvée');
        }

        if ($this->isCsrfTokenValid('delete'.$activiteevent->getId(), $request->request->get('_token'))) {
            try {
                $entityManager->remove($activiteevent);
                $entityManager->flush();
                $this->addFlash('success', 'Activité supprimée avec succès');
            } catch (\Exception $e) {
                $this->logger->error('Erreur suppression activité ID '.$activiteevent->getId().': '.$e->getMessage());
                $this->addFlash('error', 'Erreur lors de la suppression');
            }
        } else {
            $this->addFlash('error', 'Token CSRF invalide');
        }

        return $this->redirectToRoute('app_activiteevent_index', [], Response::HTTP_SEE_OTHER);
    }
    #[Route('/event/{id}/activities', name: 'event_activities', methods: ['GET'])]
public function getActivitiesByEvent(
    int $id,
    ActiviteEventRepository $activiteEventRepository
): JsonResponse {
    $activities = $activiteEventRepository->findBy(['idEvent' => $id]);

    $formatted = array_map(function ($activity) {
        return [
            'id' => $activity->getId(),
            'horaire' => $activity->getHoraire(),
            'nbrparticipant' => $activity->getNbrparticipant(),
        ];
    }, $activities);

    return new JsonResponse($formatted);
}
#[Route('/activiteevent/{id}/participer', name: 'app_activiteevent_participer')]
public function participer(Activiteevent $activiteevent, EntityManagerInterface $em): Response
{
    $activiteevent->setNbrparticipant($activiteevent->getNbrparticipant() + 1);
    $em->persist($activiteevent);
    $em->flush();

    return $this->redirectToRoute('app_activiteevent_show', ['id' => $activiteevent->getId()]);
}

}