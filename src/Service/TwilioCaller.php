<?php

namespace App\Service;

use Twilio\Rest\Client;
use Twilio\Exceptions\TwilioException;
use Psr\Log\LoggerInterface;

class TwilioCaller
{
    private Client $client;
    private string $twilioNumber;
    private string $defaultClientPhone;
    private string $defaultMessage;
    private ?LoggerInterface $logger;
    private int $timeout;

    public function __construct(
        string $accountSid,
        string $authToken,
        string $phoneNumber,
        string $defaultClientPhone,
        string $defaultMessage,
        ?LoggerInterface $logger = null,
        int $timeout = 30
    ) {
        $this->client = new Client($accountSid, $authToken);
        $this->twilioNumber = $phoneNumber;
        $this->defaultClientPhone = $defaultClientPhone;
        $this->defaultMessage = $defaultMessage;
        $this->logger = $logger;
        $this->timeout = $timeout;

        $this->validateInitialConfig();
    }

    public function sendCommunityReminder(?string $phoneNumber = null): string
    {
        try {
            $to = $phoneNumber ?? $this->defaultClientPhone;
            
            $this->validatePhoneNumber($to);

            $twiml = $this->generateTwiml();

            $this->logAttempt($to);

            $call = $this->makeTwilioCall($to, $twiml);

            $this->logSuccess($call->sid, $to);

            return $call->sid;

        } catch (TwilioException $e) {
            $this->logError($e);
            throw new \RuntimeException('Échec de l\'appel Twilio: '.$e->getMessage(), 0, $e);
        }
    }

    private function validateInitialConfig(): void
    {
        foreach ([
            'TWILIO_ACCOUNT_SID' => $this->client->accountSid,
            'TWILIO_PHONE_NUMBER' => $this->twilioNumber,
            'DEFAULT_CLIENT_PHONE' => $this->defaultClientPhone
        ] as $param => $value) {
            if (empty($value)) {
                throw new \RuntimeException("Le paramètre $param est invalide ou vide");
            }
        }
    }

    private function validatePhoneNumber(string $phone): void
    {
        if (!preg_match('/^\+[1-9]\d{1,14}$/', $phone)) {
            throw new \InvalidArgumentException("Numéro de téléphone invalide: $phone");
        }
    }

    private function generateTwiml(): string
    {
        $cleanMessage = htmlspecialchars($this->defaultMessage, ENT_QUOTES | ENT_XML1, 'UTF-8');
        
        return <<<TWIML
<Response>
    <Say voice="alice" language="fr-FR">{$cleanMessage}</Say>
</Response>
TWIML;
    }

    private function makeTwilioCall(string $to, string $twiml): \Twilio\Rest\Api\V2010\Account\CallInstance
    {
        return $this->client->calls->create(
            $to,
            $this->twilioNumber,
            [
                'twiml' => $twiml,
                'timeout' => $this->timeout
            ]
        );
    }

    private function logAttempt(string $to): void
    {
        $this->logger?->info('Tentative d\'appel Twilio', [
            'to' => $to,
            'from' => $this->twilioNumber
        ]);
    }

    private function logSuccess(string $callSid, string $to): void
    {
        $this->logger?->info('Appel Twilio réussi', [
            'callSid' => $callSid,
            'to' => $to,
            'duration' => microtime(true) - $_SERVER['REQUEST_TIME_FLOAT']
        ]);
    }

    private function logError(\Exception $e): void
    {
        $this->logger?->error('Erreur Twilio', [
            'error' => $e->getMessage(),
            'trace' => $e->getTraceAsString()
        ]);
    }
}