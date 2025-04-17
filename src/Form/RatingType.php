<?php

namespace App\Form;

use App\Entity\RatingActivity;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;
use Symfony\Component\Form\Extension\Core\Type\HiddenType;
use Symfony\Component\Form\Extension\Core\Type\IntegerType;
use Symfony\Component\Form\Extension\Core\Type\TextareaType;
use Symfony\Component\Validator\Constraints\Range;
use Symfony\Component\Validator\Constraints\NotBlank;

class RatingType extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options)
    {
        $builder
            ->add('ratingStars', IntegerType::class, [
                'attr' => [
                    'class' => 'd-none', // Champ masqué géré par l'interface à étoiles
                    'min' => 1,
                    'max' => 6
                ],
                'constraints' => [
                    new NotBlank([
                        'message' => 'Veuillez sélectionner une note entre 1 et 6 étoiles',
                    ]),
                    new Range([
                        'min' => 1,
                        'max' => 6,
                        'notInRangeMessage' => 'La note doit être comprise entre {{ min }} et {{ max }} étoiles',
                    ]),
                ],
            ])
            ->add('review', TextareaType::class, [
                'required' => false,
                'attr' => [
                    'rows' => 5,
                    'placeholder' => 'Décrivez votre expérience avec cette salle...',
                    'class' => 'form-control'
                ],
                'label' => 'Commentaire (facultatif)'
            ])
            ->add('_method', HiddenType::class, [
                'data' => 'POST',
                'mapped' => false,
            ]);
    }

    public function configureOptions(OptionsResolver $resolver)
    {
        $resolver->setDefaults([
            'data_class' => RatingActivity::class,
            'csrf_protection' => true,
            'csrf_field_name' => '_token',
            'csrf_token_id' => 'rating_item',
        ]);
    }
}