# [프로젝트 개요]

# Dto를 활용한 Bean Validation 구현

1. Dto(Data Transfer Object)
- 데이터 전송 객체
- 데이터를 전송하고 입력한 값도 검증한다.
- @Data, @NoArgsConstructor, @AllArgsConstructor, @Builder, @Getter, @Setter, @ToString, @RequiredArgsConstructor, @Value, @FieldDefaults, @SuperBuilder, @Accessors, @Wither, @With, @Singular, @Delegate, @Log, @UtilityClass, @ExtensionMethod

1. Bean Validation 의존성
- 	// 스프링 부트 Validation 의존성
     implementation 'org.springframework.boot:spring-boot-starter-validation'

2. Validation Annotation- 
- `javax.validation` 패키지의 어노테이션을 사용하여 입력값을 검증한다.
- `@NotNull`, `@Size`, `@Email`, `@Pattern`, `@Min`, `@Max`, `@AssertTrue`, `@AssertFalse`, `@DecimalMin`, `@DecimalMax`, `@Digits`, `@Past`, `@Future`, `@Positive`, `@Negative`, `@PositiveOrZero`, `@NegativeOrZero`, `@NotBlank`, `@NotEmpty`, `@Range`, `@CreditCardNumber`, `@URL`, `@Valid`, `@Validated`, `@ValidatedValue`, `@ValidatedValueUnwrapped`, `@ValidatedValueUnwrappedList`, `@ValidatedValueUnwrappedMap`, `@ValidatedValueUnwrappedArray`, `@ValidatedValueUnwrappedSet`, `@ValidatedValueUnwrappedCollection`, `@ValidatedValueUnwrappedOptional`, `@ValidatedValueUnwrappedOptionalInt`, `@ValidatedValueUnwrappedOptionalLong`, `@ValidatedValueUnwrappedOptionalDouble`, `@ValidatedValueUnwrappedOptionalFloat`, `@ValidatedValueUnwrappedOptionalShort`, `@ValidatedValueUnwrappedOptionalByte`, `@ValidatedValueUnwrappedOptionalChar`, `@ValidatedValueUnwrappedOptionalBoolean`, `@ValidatedValueUnwrappedOptionalString`, `@ValidatedValueUnwrappedOptionalBigInteger`, `@ValidatedValueUnwrappedOptionalBigDecimal`, `@ValidatedValueUnwrappedOptionalLocalDate`, `@ValidatedValueUnwrappedOptionalLocalTime`, `@ValidatedValueUnwrappedOptionalLocalDateTime`, `@ValidatedValueUnwrappedOptionalOffsetDateTime`, `@ValidatedValueUnwrappedOptionalOffsetTime`, `@ValidatedValueUnwrappedOptionalZonedDateTime`, `@ValidatedValueUnwrappedOptionalYear`, `@ValidatedValueUnwrappedOptionalYearMonth`, `@ValidatedValueUnwrappedOptionalMonthDay`, `@ValidatedValueUnwrappedOptionalInstant`, `@ValidatedValueUnwrappedOptionalDuration`, `@ValidatedValueUnwrappedOptionalPeriod`, `@ValidatedValueUnwrappedOptionalZoneId`, `@ValidatedValueUnwrappedOptionalZoneOffset`, `@ValidatedValueUnwrappedOptionalZoneRegion`, `@ValidatedValueUnwrappedOptionalZoneOffsetTransitionRule`, `@ValidatedValue     

3. 화면에 받은 데이터는 Dto를 사용해서 바인딩하고 검증한다.

4. 그렇게 받은 데이터는 BoardVo ArrayList에 저장하기 위해서 변환과정을 거쳐야 한다.
- BoardDto -> BoardVo 변환
- 

[빌더패턴]
1. Vo, Dto 편리하게 객체 생성하기 위해서 사용
