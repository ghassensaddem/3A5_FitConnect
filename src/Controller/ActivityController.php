<?php

namespace App\Controller;

use App\Entity\Activity;
use App\Form\ActivityType;
use App\Repository\ActivityRepository;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;
use Symfony\Component\HttpFoundation\File\UploadedFile;
use Symfony\Component\HttpFoundation\BinaryFileResponse;



#[Route('/activity')]
class ActivityController extends AbstractController
{
    #[Route('/', name: 'app_activity_index', methods: ['GET'])]
public function index(ActivityRepository $activityRepository): Response
{   
    $activities = $activityRepository->findAll();
    
    // Debug: Vérifier l'existence des fichiers
    foreach ($activities as $activity) {
        if ($activity->getIconActivity()) {
            $filePath = $this->getParameter('upload_directory').'/'.$activity->getIconActivity();
            if (!file_exists($filePath)) {
                $this->addFlash('warning', 'Fichier manquant: '.$activity->getIconActivity());
            }
        }
    }
    
    return $this->render('activity/index.html.twig', [
        'activities' => $activities,
    ]);
}

    
    #[Route('/new', name: 'app_activity_new', methods: ['GET', 'POST'])]
    public function new(Request $request, EntityManagerInterface $em): Response
{
    $activity = new Activity();
    $form = $this->createForm(ActivityType::class, $activity);
    $form->handleRequest($request);

    try {
        if ($form->isSubmitted() && $form->isValid()) {
            /** @var UploadedFile $imageFile */
            $imageFile = $form->get('imageFile')->getData();
            
            if ($imageFile) {
                $newFilename = $this->uploadFile($imageFile);
                $activity->setIconActivity($newFilename);
            }

            $em->persist($activity);
            $em->flush();

            $this->addFlash('success', 'Activité créée avec succès');
            return $this->redirectToRoute('app_activity_index');
        }
    } catch (\Exception $e) {
        $this->addFlash('error', 'Erreur: '.$e->getMessage());
    }

    return $this->render('activity/new.html.twig', [
        'form' => $form->createView(),
    ]);
}
    private function uploadFile(UploadedFile $file): string
{
    try {
        // Vérification du fichier source
        if (!$file->isValid()) {
            throw new \RuntimeException('Erreur lors du téléchargement: '.$file->getErrorMessage());
        }

        $originalFilename = pathinfo($file->getClientOriginalName(), PATHINFO_FILENAME);
        $safeFilename = preg_replace('/[^A-Za-z0-9_]/', '', $originalFilename);
        $safeFilename = strtolower($safeFilename);
        $newFilename = $safeFilename.'-'.uniqid().'.'.$file->guessExtension();

        $targetDir = $this->getParameter('upload_directory');
        
        // Création du répertoire si nécessaire
        if (!file_exists($targetDir)) {
            mkdir($targetDir, 0777, true);
        }

        $file->move($targetDir, $newFilename);

        // Vérification que le fichier a bien été déplacé
        if (!file_exists($targetDir.'/'.$newFilename)) {
            throw new \RuntimeException('Le déplacement du fichier a échoué');
        }

        return $newFilename;
    } catch (\Exception $e) {
        $this->addFlash('error', 'Erreur lors de l\'upload: '.$e->getMessage());
        throw $e;
    }
}

    #[Route('/{idActivity}', name: 'app_activity_show', methods: ['GET'])]
    public function show(Activity $activity): Response
    {
        return $this->render('activity/show.html.twig', [
            'activity' => $activity,
        ]);
    }

    #[Route('/{idActivity}/edit', name: 'app_activity_edit', methods: ['GET', 'POST'])]
    public function edit(Request $request, Activity $activity, EntityManagerInterface $entityManager): Response
    {
        $form = $this->createForm(ActivityType::class, $activity);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $entityManager->flush();

            return $this->redirectToRoute('app_activity_index');
        }

        return $this->render('activity/edit.html.twig', [
            'activity' => $activity,
            'form' => $form->createView(),
        ]);
    }

    #[Route('/{idActivity}', name: 'app_activity_delete', methods: ['POST'])]
public function delete(Request $request, Activity $activity, EntityManagerInterface $entityManager): Response
{
    if ($this->isCsrfTokenValid('delete'.$activity->getIdActivity(), $request->request->get('_token'))) {
        $entityManager->remove($activity);
        $entityManager->flush();
        $this->addFlash('success', 'Activité supprimée avec succès');
    }

    return $this->redirectToRoute('app_activity_index');
}
#[Route('/img/{filename}', name: 'app_image_serve', requirements: ['filename' => '.+\.(jpg|jpeg|png|gif|svg)$'], priority: -10)]
    public function serveImage(string $filename): Response
    {
        $filePath = $this->getParameter('upload_directory').'/'.$filename;
        
        if (!file_exists($filePath)) {
            throw $this->createNotFoundException("L'image $filename n'existe pas");
        }

        return new BinaryFileResponse($filePath);
    }
}
