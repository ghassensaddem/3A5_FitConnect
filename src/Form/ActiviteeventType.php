<?php

namespace App\Form;

use App\Entity\Activiteevent;
use App\Entity\Event;
use App\Entity\Typeactivite;
use Symfony\Bridge\Doctrine\Form\Type\EntityType;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\Extension\Core\Type\IntegerType;
use Symfony\Component\Form\Extension\Core\Type\TextType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;
use Symfony\Component\Validator\Constraints\NotBlank;
use Symfony\Component\Validator\Constraints\Range;

class ActiviteeventType extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options): void
    {
        $builder
            ->add('horaire', TextType::class, [
                'label' => 'Horaire (ex: 14h30)',
                'attr' => [
                    'placeholder' => 'Entrez l\'horaire'
                ],
                'constraints' => [
                    new NotBlank([
                        'message' => 'Le champ horaire est obligatoire.'
                    ])
                ]
            ])
            ->add('nbrparticipant', IntegerType::class, [
                'label' => 'Nombre de participants',
                'attr' => [
                    'min' => 20,
                    'max' => 50,
                    'placeholder' => 'Entre 20 et 50'
                ],
                'constraints' => [
                    new NotBlank([
                        'message' => 'Le nombre de participants est obligatoire.'
                    ]),
                    new Range([
                        'min' => 20,
                        'max' => 50,
                        'notInRangeMessage' => 'Le nombre de participants doit être compris entre {{ min }} et {{ max }}.'
                    ])
                ]
            ])
            ->add('event', EntityType::class, [
                'class' => Event::class,
                'label' => 'Événement associé',
                'choice_label' => function(Event $event) {
                    return  $event->getDate();
                },
                'placeholder' => 'Sélectionnez un événement',
                'query_builder' => function (\App\Repository\EventRepository $er) {
                    return $er->createQueryBuilder('e')
                        ->orderBy('e.date', 'DESC');
                },
                'constraints' => [
                    new NotBlank([
                        'message' => 'Veuillez sélectionner un événement.'
                    ])
                ]
            ])
            ->add('typeactivite', EntityType::class, [
                'class' => Typeactivite::class,
                'label' => 'Type d\'activité',
                'choice_label' => 'title',
                'placeholder' => 'Choisissez un type',
                'query_builder' => function (\App\Repository\TypeactiviteRepository $er) {
                    return $er->createQueryBuilder('t')
                        ->orderBy('t.title', 'ASC');
                },
                'constraints' => [
                    new NotBlank([
                        'message' => 'Veuillez sélectionner un type d\'activité.'
                    ])
                ]
            ])
        ;
    }

    public function configureOptions(OptionsResolver $resolver): void
    {
        $resolver->setDefaults([
            'data_class' => Activiteevent::class,
        ]);
    }
}
