export const passwordRegexp = /^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\S+$).{8,}$/;
export const emailRegexp = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
export const jobRoles = [
    'Administrative/Facilities',
    'Accounting/Finance',
    'Business Development',
    'Business Owner',
    'Customer Support',
    'Data/Analytics/Business Intelligence',
    'Design',
    'Engineering (Software)',
    'Marketing',
    'Media/Communications',
    'Operations',
    'Product Management',
    'Program/Project Management',
    'Research',
    'Sales',
    'Other'
];