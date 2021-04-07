package com.dilwar.annotations

import javax.validation.Constraint
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import javax.validation.Payload
import kotlin.reflect.KClass


@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
@Constraint(validatedBy = [GRecyclerViewFactoryValidator::class])
annotation class GRecyclerViewFactory(
    val classes: Array<KClass<*>>,
    val enableDataBinding: Boolean = false,
    val message: String = "The value of year must be between 1459 and next year!",
    val group: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)

class GRecyclerViewFactoryValidator : ConstraintValidator<GRecyclerViewFactory, Any> {
    override fun isValid(value: Any?, context: ConstraintValidatorContext?): Boolean {
        return false
    }
}
