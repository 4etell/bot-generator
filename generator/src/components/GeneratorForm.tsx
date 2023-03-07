import React, {useState} from "react";

import {
    Box, Button, FormControl, FormErrorMessage, FormLabel, Input, InputGroup, InputRightElement,
    NumberDecrementStepper,
    NumberIncrementStepper,
    NumberInput, NumberInputField, NumberInputStepper
} from "@chakra-ui/react";
import {Field, Form, Formik} from "formik";

interface FormValues {
    email: string;
    domain: string;
    adminUsername: string;
    adminPassword: string;
    telegramToken: string;
    messageBrokerReplicas: number;
    messageHandlerReplicas: number;
}

function GeneratorForm() {

    const [showPassword, setShowPassword] = useState(false);
    const [showTelegramToken, setShowTelegramToken] = useState(false);

    const initialValues: FormValues = {
        email: '',
        domain: '',
        adminUsername: '',
        adminPassword: '',
        telegramToken: '',
        messageBrokerReplicas: 1,
        messageHandlerReplicas: 1
    };

    const onSubmit = (values: FormValues, actions: any) => {
        setTimeout(() => {
            alert(JSON.stringify(values, null, 2))
            actions.setSubmitting(false)
        }, 1000)
    }

    const validateEmail = (value: string) => {
        let error
        if (!value) {
            error = 'Required'
        } else if (!/^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,4}$/i.test(value)) {
            error = "Invalid email address"
        }
        return error
    }

    const validateDomain = (value: string) => {
        let error
        if (!value) {
            error = 'Required'
        }
        return error
    }

    const validateAdminUsername = (value: string) => {
        let error
        if (!value) {
            error = 'Required'
        }
        return error
    }

    const validateAdminPassword = (value: string) => {
        let error
        if (!value) {
            error = 'Required'
        }
        return error
    }

    const validateTelegramToken = (value: string) => {
        let error
        if (!value) {
            error = 'Required'
        }
        return error
    }
    return (
        <Box p={15} maxW={"1024px"} m="auto">
            <Formik
                initialValues={initialValues}
                onSubmit={onSubmit}
            >
                {(props) => (
                    <Form>
                        <Field name='email' validate={validateEmail}>
                            {({field, form}: { field: any; form: any }) => (
                                <FormControl isInvalid={form.errors.email && form.touched.email}>
                                    <FormLabel>Email</FormLabel>
                                    <Input {...field} placeholder="Enter email..."/>
                                    <FormErrorMessage>{form.errors.email}</FormErrorMessage>
                                </FormControl>
                            )}
                        </Field>
                        <Field name='domain' validate={validateDomain}>
                            {({field, form}: { field: any; form: any }) => (
                                <FormControl mt={4} isInvalid={form.errors.domain && form.touched.domain}>
                                    <FormLabel>Domain</FormLabel>
                                    <Input {...field} placeholder="Enter your server domain (example.com)..."/>
                                    <FormErrorMessage>{form.errors.domain}</FormErrorMessage>
                                </FormControl>
                            )}
                        </Field>
                        <Field name='adminUsername' validate={validateAdminUsername}>
                            {({field, form}: { field: any; form: any }) => (
                                <FormControl mt={4} isInvalid={form.errors.adminUsername && form.touched.adminUsername}>
                                    <FormLabel>Admin username</FormLabel>
                                    <Input {...field} placeholder="Enter username for admin panel..."/>
                                    <FormErrorMessage>{form.errors.adminUsername}</FormErrorMessage>
                                </FormControl>
                            )}
                        </Field>
                        <Field name='adminPassword' validate={validateAdminPassword}>
                            {({field, form}: { field: any; form: any }) => (
                                <FormControl mt={4} isInvalid={form.errors.adminPassword && form.touched.adminPassword}>
                                    <FormLabel>Admin password</FormLabel>
                                    <InputGroup size='md'>
                                        <Input
                                            {...field}
                                            pr='4.5rem'
                                            type={showPassword ? 'text' : 'password'}
                                            placeholder='Enter password for admin panel...'
                                        />
                                        <InputRightElement width='4.5rem'>
                                            <Button h='1.75rem' size='sm'
                                                    onClick={() => setShowPassword(!showPassword)}>
                                                {showPassword ? 'Hide' : 'Show'}
                                            </Button>
                                        </InputRightElement>
                                    </InputGroup>
                                    <FormErrorMessage>{form.errors.adminPassword}</FormErrorMessage>
                                </FormControl>
                            )}
                        </Field>
                        <Field name='telegramToken' validate={validateTelegramToken}>
                            {({field, form}: { field: any; form: any }) => (
                                <FormControl mt={4} isInvalid={form.errors.telegramToken && form.touched.telegramToken}>
                                    <FormLabel>Telegram token</FormLabel>
                                    <InputGroup size='md'>
                                        <Input
                                            {...field}
                                            pr='4.5rem'
                                            type={showTelegramToken ? 'text' : 'password'}
                                            placeholder='Enter telegram token...'
                                        />
                                        <InputRightElement width='4.5rem'>
                                            <Button h='1.75rem' size='sm'
                                                    onClick={() => setShowTelegramToken(!showTelegramToken)}>
                                                {showTelegramToken ? 'Hide' : 'Show'}
                                            </Button>
                                        </InputRightElement>
                                    </InputGroup>
                                    <FormErrorMessage>{form.errors.telegramToken}</FormErrorMessage>
                                </FormControl>
                            )}
                        </Field>
                        <Field name='messageBrokerReplicas'>
                            {({field, form}: { field: any; form: any }) => (
                                <FormControl mt={4}>
                                    <FormLabel>Message-broker replicas</FormLabel>
                                    <NumberInput  defaultValue={1} min={1} max={10} onChange={val=>form.setFieldValue(field.name, val)}  >
                                    <NumberInputField/>
                                        <NumberInputStepper>
                                            <NumberIncrementStepper/>
                                            <NumberDecrementStepper/>
                                        </NumberInputStepper>
                                    </NumberInput>
                                </FormControl>
                            )}
                        </Field>
                        <Field name='messageHandlerReplicas'>
                            {({field, form}: { field: any; form: any }) => (
                                <FormControl mt={4}>
                                    <FormLabel>Message-handler replicas</FormLabel>
                                    <NumberInput  defaultValue={1} min={1} max={10} onChange={val=>form.setFieldValue(field.name, val)}  >
                                        <NumberInputField/>
                                        <NumberInputStepper>
                                            <NumberIncrementStepper/>
                                            <NumberDecrementStepper/>
                                        </NumberInputStepper>
                                    </NumberInput>
                                </FormControl>
                            )}
                        </Field>
                        <Button
                            mt={4}
                            pr={8}
                            pl={8}
                            colorScheme='teal'
                            isLoading={props.isSubmitting}
                            type='submit'
                        >
                            Generate
                        </Button>
                    </Form>
                )}
            </Formik>
        </Box>
    );
}

export default GeneratorForm;
