<?php
namespace App\Form;

use App\Entity\Coach;
use App\Repository\ActivityRepository;
use App\Repository\SallesportifRepository;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;
use Symfony\Bridge\Doctrine\Form\Type\EntityType;
use Symfony\Component\Form\Extension\Core\Type\ChoiceType;
use Symfony\Component\Form\Extension\Core\Type\FileType;

class CoachType extends AbstractType
{
    private $activityRepository;
    private $salleRepository;

    public function __construct(ActivityRepository $activityRepository, SallesportifRepository $salleRepository)
    {
        $this->activityRepository = $activityRepository;
        $this->salleRepository = $salleRepository;
    }

    public function buildForm(FormBuilderInterface $builder, array $options): void
    {
        $builder
            ->add('nom', null, [
                'label' => 'Nom',
                'required' => false,
                'attr' => ['class' => 'form-control', 'placeholder' => 'Entrez le nom'],
            ])
            ->add('prenom', null, [
                'label' => 'Prénom',
                'required' => false,
                'attr' => ['class' => 'form-control', 'placeholder' => 'Entrez le prénom'],
            ])
            ->add('sexe', ChoiceType::class, [
                'label' => 'Sexe',
                'choices' => [
                    'Homme' => 'Homme',
                    'Femme' => 'Femme',
                ],
                'attr' => ['class' => 'form-control'],
            ])
            ->add('mdp', null, [
                'label' => 'Mot de passe',
                'required' => false,
                'attr' => ['class' => 'form-control', 'placeholder' => 'Entrez le mot de passe'],
            ])
            ->add('dateNaissance', null, [
                'label' => 'Date de naissance',
                'required' => false,
                'widget' => 'single_text',
                'attr' => ['class' => 'form-control'],
            ])
            ->add('email', null, [
                'label' => 'Email',
                'required' => false,
                'attr' => ['class' => 'form-control', 'placeholder' => 'Entrez l\'email'],
            ])
            ->add('lieuEngagement', ChoiceType::class, [
                'label' => 'Lieu d\'engagement',
                'choices' => $this->getLieuEngagementChoices(),
                'attr' => ['class' => 'form-control'],
            ])
            ->add('specialite', ChoiceType::class, [
                'label' => 'Spécialité',
                'choices' => $this->getSpecialiteChoices(),
                'attr' => ['class' => 'form-control'],
            ])
            ->add('image', FileType::class, [
                'label' => 'Photo de profil',
                'mapped' => false,
                'required' => false,
                'attr' => ['class' => 'form-control'],
            ]);
    }

    public function configureOptions(OptionsResolver $resolver): void
    {
        $resolver->setDefaults([
            'data_class' => Coach::class,
        ]);
    }

    private function getLieuEngagementChoices(): array
    {
        $lieux = $this->salleRepository->findAll();
        $choices = [];
        foreach ($lieux as $lieu) {
            $choices[$lieu->getAddresseSalle()] = $lieu->getAddresseSalle(); // Clé => Valeur
        }
        return $choices;
    }

    private function getSpecialiteChoices(): array
    {
        $activities = $this->activityRepository->afficherNomActivity();
        $choices = [];
        foreach ($activities as $activity) {
            $choices[$activity['nomActivity']] = $activity['nomActivity']; // Clé => Valeur
        }
        return $choices;
    }
}