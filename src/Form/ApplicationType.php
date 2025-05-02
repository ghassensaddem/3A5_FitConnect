<?php

namespace App\Form;

use App\Entity\Application;
use App\Entity\Client;
use App\Entity\Programme;
use Symfony\Bridge\Doctrine\Form\Type\EntityType;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;
use Symfony\Component\Form\Extension\Core\Type\DateType;
use Symfony\Component\Form\Extension\Core\Type\TimeType;
class ApplicationType extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options): void
    {
        $builder
        ->add('dateDebut', DateType::class, [
            'widget' => 'single_text',
            'input' => 'datetime',
            'required' => true,
            'html5' => true,
            'label' => 'Date',
        ])
        ->add('dateFin', DateType::class, [
            'widget' => 'single_text',
            'input' => 'datetime',
            'required' => true,
            'html5' => true,
            'label' => 'Date',
        ])
            ->add('idProgramme', EntityType::class, [
                'class' => Programme::class,
                'choice_label' => 'description', // Remplace 'id' par un champ plus pertinent si nécessaire
                'label' => 'Programme',
                'attr' => ['class' => 'form-control']
            ])
            ->add('idClient', EntityType::class, [
                'class' => Client::class,
                'choice_label' => 'nom', // Remplace 'id' par 'nom' ou un autre champ pertinent
                'label' => 'Client',
                'attr' => ['class' => 'form-control']
            ]);
    }

    public function configureOptions(OptionsResolver $resolver): void
    {
        $resolver->setDefaults([
            'data_class' => Application::class,
        ]);
    }
}
